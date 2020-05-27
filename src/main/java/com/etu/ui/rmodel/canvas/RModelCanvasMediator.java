package com.etu.ui.rmodel.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.event.dto.RModelLinkEvent;
import com.etu.infrastructure.event.dto.RModelRelationEvent;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSide;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.relational.RModelService;
import com.etu.ui.AbstractMediator;
import com.etu.ui.UiFactory;
import com.etu.ui.rmodel.link.node.RModelLinkMediator.RModelJoinPointPair;
import com.etu.ui.rmodel.link.node.RModelLinkView;
import com.etu.ui.rmodel.relation.node.RModelRelationView;
import com.etu.ui.util.DragContext;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class RModelCanvasMediator extends AbstractMediator {

    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private RModelService rModelFacade;

    private DragContext dragContext;

    private List<RModelRelationView> relationViews = new ArrayList<>();
    private ListProperty<Parent> relationViewsRoots = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    private List<RModelLinkView> linkViews = new ArrayList<>();
    private ListProperty<Parent> linkViewsRoots = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(EventTypes.RM_RELATION_CREATED, this::addRelation);
        eventFacade.addEventListener(EventTypes.RM_RELATION_REMOVED, this::removeRelation);
        eventFacade.addEventListener(EventTypes.RM_LINK_CREATED, this::addLink);
        eventFacade.addEventListener(EventTypes.RM_LINK_REMOVED, this::removeLink);
        eventFacade.addEventListener(EventTypes.RM_GENERATED, this::refreshCanvas);
        eventFacade.addEventListener(EventTypes.PROJECT_OPENED, this::refreshCanvas);
        eventFacade.addEventListener(EventTypes.PROJECT_CLOSED, this::clearCanvas);
    }

    private void clearCanvas(Event event) {
        relationViews.clear();
        relationViewsRoots.clear();

        linkViews.clear();
        linkViewsRoots.clear();
    }

    private void refreshCanvas(Event event) {
        rModelFacade.getCurrentRelations().forEach(this::addRelation);
        rModelFacade.getCurrentLinks().forEach(this::addLink);
    }

    private void addRelation(RModelRelationEvent event) {
        RModelRelation table = event.getRelation();
        addRelation(table);
    }

    private void addRelation(RModelRelation relation) {
        RModelRelationView tableView = uiFactory.createView(RModelRelationView.class);
        tableView.getMediator().configure(relation);
        tableView.getMediator().setOnMove(this::updateJoinPointsLater);
        makeNodeDraggable(tableView.getRootNode());

        relationViews.add(tableView);
        relationViewsRoots.add(tableView.getRootNode());
    }

    private void removeRelation(RModelRelationEvent event) {
        RModelRelation table = event.getRelation();
        RModelRelationView tableView = getTableViewFor(table);
        relationViews.remove(tableView);
        relationViewsRoots.remove(tableView.getRootNode());
    }

    private void addLink(RModelLinkEvent event) {
        RModelLink link = event.getLink();
        addLink(link);
    }

    private void addLink(RModelLink link) {
        RModelLinkView relationView = uiFactory.createView(RModelLinkView.class);
        relationView.getMediator().configure(link);

        linkViews.add(relationView);
        linkViewsRoots.add(relationView.getRootNode());

        link.getLinkedAttributesMap().forEach((attributeFrom, attributeTo) -> {
            RModelRelation relationFrom = attributeFrom.getRelation();

            RModelJoinPoint jpFrom = new RModelJoinPoint();
            jpFrom.setRelation(link);
            jpFrom.setTable(relationFrom);
            jpFrom.setAttribute(attributeFrom);
            getTableViewFor(relationFrom).getMediator().addJoinPoint(jpFrom);

            RModelRelation relationTo = attributeTo.getRelation();
            RModelJoinPoint jpTo = new RModelJoinPoint();
            jpTo.setRelation(link);
            jpTo.setTable(relationTo);
            jpTo.setAttribute(attributeTo);
            getTableViewFor(relationTo).getMediator().addJoinPoint(jpTo);

            RModelJoinPointPair pair = new RModelJoinPointPair();
            pair.setJoinPointFrom(jpFrom);
            pair.setJoinPointTo(jpTo);

            relationView.getMediator().addJoinPointPair(pair);
        });

        updateJoinPointsLater();
    }


    private void removeLink(RModelLinkEvent event) {
        RModelLink relation = event.getLink();
        RModelLinkView relationView = getRelationViewFor(relation);
        linkViews.remove(relationView);
        linkViewsRoots.remove(relationView.getRootNode());

        getTableViewFor(relation.getLinkSideFrom()).getMediator().removeJoinPointsFor(relation);
        getTableViewFor(relation.getLinkSideTo()).getMediator().removeJoinPointsFor(relation);

        updateJoinPointsLater();
    }

    private RModelRelationView getTableViewFor(RModelLinkSide sideDto) {
        return getTableViewFor(sideDto.getRelation());
    }

    private RModelRelationView getTableViewFor(RModelRelation tableDto) {
        return relationViews.stream()
                .filter(view -> view.getMediator().relationProperty().get() == tableDto)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }


    private RModelLinkView getRelationViewFor(RModelLink relationDto) {
        return linkViews.stream()
                .filter(view -> view.getMediator().getLink().getId().equals(relationDto.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    protected void makeNodeDraggable(Node node) {
        node.setOnDragDetected(
                event -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString("");

                    node.startDragAndDrop(TransferMode.MOVE).setContent(content);
                    node.setMouseTransparent(true);

                    dragContext = new DragContext(event.getX(), event.getY(), node);

                    event.consume();
                }
        );
    }

    private void updateJoinPointsLater() {
        Platform.runLater(this::updateJoinPoints);
    }

    private void updateJoinPoints() {
        relationViews.forEach(view -> view.getMediator().updateJoinPoints());
    }

    public ObservableList<Parent> getRelationViewsRoots() {
        return relationViewsRoots.get();
    }

    public ListProperty<Parent> relationViewsRootsProperty() {
        return relationViewsRoots;
    }

    public ListProperty<Parent> linkViewsRootsProperty() {
        return linkViewsRoots;
    }

    public void onMouseClick() {
        workflowService.startProjectWorkflow(WorkflowType.RM_DISCARD_CANVAS_SELECTION);
    }

    public void dragOver(DragEvent event) {
        double x = event.getX() - dragContext.getDeltaX();
        double y = event.getY() - dragContext.getDeltaY();

        dragContext.getDraggedNode().relocate(x, y);

        event.consume();

    }

    public void dragDone(DragEvent event) {
        dragContext.getDraggedNode().setMouseTransparent(false);
        dragContext = null;

        event.consume();
    }
}
