package com.etu.ui.ermodel.canvas;


import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.ERModelEntityEvent;
import com.etu.infrastructure.event.dto.ERModelRelationEvent;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.er.ERModelService;
import com.etu.ui.AbstractMediator;
import com.etu.ui.UiFactory;
import com.etu.ui.ermodel.entity.node.ERModelEntityView;
import com.etu.ui.ermodel.relation.node.ERModelRelationView;
import com.etu.ui.util.DragContext;
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
public class ERModelCanvasMediator extends AbstractMediator {

    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private ERModelService erModelFacade;

    private DragContext dragContext;

    private List<ERModelEntityView> entityViews = new ArrayList<>();
    private ListProperty<Parent> entityViewsRoots = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    private List<ERModelRelationView> relationViews = new ArrayList<>();
    private ListProperty<Parent> relationViewsRoots = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    private boolean isRelationCreating = false;

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(EventTypes.ERM_ENTITY_CREATED, this::addEntity);
        eventFacade.addEventListener(EventTypes.ERM_ENTITY_REMOVED, this::removeEntity);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATED, this::addRelation);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_REMOVED, this::removeRelation);
        eventFacade.addEventListener(EventTypes.PROJECT_OPENED, this::refreshCanvas);
        eventFacade.addEventListener(EventTypes.PROJECT_CLOSED, this::clearCanvas);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_STARTED, e -> isRelationCreating = true);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_FINISHED, e -> isRelationCreating = false);
    }

    private void clearCanvas(Event event) {
        entityViews.clear();
        entityViewsRoots.clear();

        relationViews.clear();
        relationViewsRoots.clear();
    }

    private void refreshCanvas(Event event) {
        erModelFacade.getCurrentEntities().forEach(this::addEntity);
        erModelFacade.getCurrentRelations().forEach(this::addRelation);
    }

    private void addEntity(ERModelEntityEvent event) {
        ERModelEntity entity = event.getEntity();
        addEntity(entity);
    }

    private void addEntity(ERModelEntity entity) {
        ERModelEntityView entityView = uiFactory.createView(ERModelEntityView.class);
        entityView.getMediator().configure(entity);
        entityView.getMediator().setOnMove(this::updateJoinPoints);
        makeNodeDraggable(entityView.getRootNode());

        entityViews.add(entityView);
        entityViewsRoots.add(entityView.getRootNode());
    }

    private void removeEntity(ERModelEntityEvent event) {
        ERModelEntity entity = event.getEntity();
        ERModelEntityView entityView = getEntityViewFor(entity);
        entityViews.remove(entityView);
        entityViewsRoots.remove(entityView.getRootNode());
    }

    private void removeRelation(ERModelRelationEvent event) {
        ERModelRelation relation = event.getRelation();
        ERModelRelationView relationView = getRelationViewFor(relation);
        relationViews.remove(relationView);
        relationViewsRoots.remove(relationView.getRootNode());

        relation.getRelationSides().stream()
                .map(ERModelRelationSide::getEntity)
                .map(this::getEntityViewFor)
                .forEach(entityView -> entityView.getMediator().removeJoinPointsFor(relation));
    }


    private void updateJoinPoints() {
        entityViews.forEach(view -> view.getMediator().updateJoinPoints());
    }

    private void addRelation(ERModelRelationEvent event) {
        ERModelRelation relation = event.getRelation();
        addRelation(relation);
    }

    private void addRelation(ERModelRelation relation) {

        ERModelRelationView relationView = uiFactory.createView(ERModelRelationView.class);
        relationView.getMediator().configure(relation);

        relationViews.add(relationView);
        relationViewsRoots.add(relationView.getRootNode());

        relation.getRelationSides().forEach(
                relationSide -> {
                    ERModelEntity entity = relationSide.getEntity();
                    ERModelJoinPoint jp = new ERModelJoinPoint();
                    jp.setRelation(relation);
                    jp.setEntity(entity);
                    getEntityViewFor(entity).getMediator().addJoinPoint(jp);
                    relationView.getMediator().addJoinPoint(jp);
                }
        );
    }

    private ERModelEntityView getEntityViewFor(ERModelEntity entityDto) {
        return entityViews.stream()
                .filter(view -> view.getMediator().entityProperty().get() == entityDto)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private ERModelRelationView getRelationViewFor(ERModelRelation relationDto) {
        return relationViews.stream()
                .filter(view -> view.getMediator().getRelation().getId().equals(relationDto.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public ObservableList<Parent> getEntityViewsRoots() {
        return entityViewsRoots.get();
    }

    public ListProperty<Parent> entityViewsRootsProperty() {
        return entityViewsRoots;
    }

    public ObservableList<Parent> getRelationViewsRoots() {
        return relationViewsRoots.get();
    }

    public ListProperty<Parent> relationViewsRootsProperty() {
        return relationViewsRoots;
    }


    private void makeNodeDraggable(Parent node) {
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

    public void onMouseClick() {
        if (!isRelationCreating) {
            workflowService.startProjectWorkflow(WorkflowType.ERM_DISCARD_CANVAS_SELECTION);
        }
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
