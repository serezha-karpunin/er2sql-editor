package com.etu.ui.rmodel.relation.node;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.ui.AbstractMediator;
import com.etu.ui.UiFactory;
import com.etu.ui.rmodel.canvas.RModelJoinPoint;
import com.etu.ui.rmodel.canvas.RModelJoinPoint.RModelJoinPointSide;
import com.etu.ui.rmodel.relation.node.attribute.RModelRelationAttributeView;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
@Scope("prototype")
public class RModelRelationMediator extends AbstractMediator {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private UiFactory uiFactory;

    private ObjectProperty<RModelRelation> relation = new SimpleObjectProperty<>();
    private ListProperty<RModelJoinPoint> joinPoints = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    private Runnable onMove;

    private List<RModelRelationAttributeView> relationAttributeViews = new ArrayList<>();
    private ListProperty<Parent> relationAttributeViewsRoots = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    private boolean isLinkCreating = false;

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(EventTypes.RM_LINK_CREATION_STARTED, e -> isLinkCreating = true);
        eventFacade.addEventListener(EventTypes.RM_LINK_CREATION_FINISHED, e -> isLinkCreating = false);
    }

    public void configure(RModelRelation dto) {
        relation.set(dto);
        relation.get().boundsProperty().addListener((observable, oldValue, newValue) -> onMove.run());

        relation.get().getAttributes().forEach(this::addAttribute);
        relation.get().attributesProperty().addListener(this::onAttributesChanged);
    }

    private void onAttributesChanged(ListChangeListener.Change<? extends RModelRelationAttribute> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::addAttribute);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(this::removeAttribute);
            }
        }

        if (onMove != null) {
            onMove.run();
        }
    }

    private void addAttribute(RModelRelationAttribute attributeDto) {
        RModelRelationAttributeView attributeView = uiFactory.createView(RModelRelationAttributeView.class);
        attributeView.getMediator().configure(attributeDto);

        relationAttributeViews.add(attributeView);
        relationAttributeViewsRoots.add(attributeView.getRootNode());
    }

    private void removeAttribute(RModelRelationAttribute attribute) {
        RModelRelationAttributeView attributeView = getTableAttributeViewFor(attribute);

        relationAttributeViews.remove(attributeView);
        relationAttributeViewsRoots.remove(attributeView.getRootNode());
    }


    private RModelRelationAttributeView getTableAttributeViewFor(RModelRelationAttribute attribute) {
        return relationAttributeViews.stream()
                .filter(view -> Objects.equals(view.getMediator().getAttribute(), attribute))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public void onMouseClicked() {
        if (isLinkCreating) {
            workflowService.startRModelRelationWorkflow(WorkflowType.RM_SELECT_RELATION_TO_CREATE_LINK, relation.get());
        } else {
            workflowService.startRModelRelationWorkflow(WorkflowType.RM_SELECT_RELATION_TO_EDIT, relation.get());
        }
    }

    public void edit() {
        workflowService.startRModelRelationWorkflow(WorkflowType.RM_EDIT_RELATION, relation.get());
    }

    public void remove() {
        workflowService.startRModelRelationWorkflow(WorkflowType.RM_REMOVE_RELATION, relation.get());
    }

    public ObjectProperty<RModelRelation> relationProperty() {
        return relation;
    }

    public void setOnMove(Runnable onMove) {
        this.onMove = onMove;
    }

    public void addJoinPoint(RModelJoinPoint joinPoint) {
        joinPoints.add(joinPoint);
    }

    public void removeJoinPointsFor(RModelLink relation) {
        joinPoints.removeIf(jp -> Objects.equals(jp.getRelation(), relation));
    }


    private List<RModelRelation> getLinkedRelations(RModelJoinPoint joinPoint) {
        RModelLink relation = joinPoint.getRelation();
        return Arrays.asList(relation.getLinkSideFrom().getRelation(), relation.getLinkSideTo().getRelation());
    }

    public ObservableList<Parent> getRelationAttributeViewsRoots() {
        return relationAttributeViewsRoots.get();
    }

    public ListProperty<Parent> relationAttributeViewsRootsProperty() {
        return relationAttributeViewsRoots;
    }

    private List<RModelJoinPoint> getJoinPointsBySide(RModelJoinPointSide side) {
        return joinPoints.stream()
                .filter(jp -> side.equals(jp.getSide()))
                .collect(toList());
    }

    public void updateJoinPoints() {
        for (RModelJoinPoint joinPoint : joinPoints) {
            List<RModelRelation> linkedRelations = getLinkedRelations(joinPoint);
            double sumX = linkedRelations.stream()
                    .map(RModelRelation::getCenterX)
                    .reduce(Double::sum)
                    .orElse(0d);

            double centerX = sumX / linkedRelations.size();
            if (relation.get().getX() + relation.get().getWidth() < centerX) {
                joinPoint.setSide(RModelJoinPointSide.RIGHT);
            } else if (relation.get().getX() > centerX) {
                joinPoint.setSide(RModelJoinPointSide.LEFT);
            } else {
                joinPoint.setSide(RModelJoinPointSide.RIGHT);
            }
        }


        List<RModelJoinPoint> leftJoinPoints = getJoinPointsBySide(RModelJoinPointSide.LEFT);
        Map<RModelRelationAttribute, List<RModelJoinPoint>> groupedLeftJoinPoints = leftJoinPoints.stream()
                .collect(groupingBy(RModelJoinPoint::getAttribute));

        for (Map.Entry<RModelRelationAttribute, List<RModelJoinPoint>> entry : groupedLeftJoinPoints.entrySet()) {
            RModelRelationAttribute attribute = entry.getKey();
            List<RModelJoinPoint> jps = entry.getValue();

            double delta = attribute.getHeight() / (jps.size() + 1);

            for (int i = 0; i < jps.size(); i++) {
                RModelJoinPoint jp = jps.get(i);
                jp.setX(attribute.getRelation().getX());
                jp.setY(attribute.getRelation().getY() + attribute.getY() + delta * (i + 1));
            }
        }

        List<RModelJoinPoint> rightJoinPoints = getJoinPointsBySide(RModelJoinPointSide.RIGHT);
        Map<RModelRelationAttribute, List<RModelJoinPoint>> groupedRightJoinPoints = rightJoinPoints.stream()
                .collect(groupingBy(RModelJoinPoint::getAttribute));

        for (Map.Entry<RModelRelationAttribute, List<RModelJoinPoint>> entry : groupedRightJoinPoints.entrySet()) {
            RModelRelationAttribute attribute = entry.getKey();
            List<RModelJoinPoint> jps = entry.getValue();

            double delta = attribute.getHeight() / (jps.size() + 1);

            for (int i = 0; i < jps.size(); i++) {
                RModelJoinPoint jp = jps.get(i);
                jp.setX(attribute.getRelation().getX() + attribute.getRelation().getWidth());
                jp.setY(attribute.getRelation().getY() + attribute.getY() + delta * (i + 1));
            }
        }

    }
}
