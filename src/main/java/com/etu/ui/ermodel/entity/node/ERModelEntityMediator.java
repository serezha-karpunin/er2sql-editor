package com.etu.ui.ermodel.entity.node;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.ui.AbstractMediator;
import com.etu.ui.UiFactory;
import com.etu.ui.ermodel.canvas.ERModelJoinPoint;
import com.etu.ui.ermodel.canvas.ERModelJoinPoint.ERModelJoinPointSide;
import com.etu.ui.ermodel.entity.node.attribute.ERModelEntityAttributeView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@Scope("prototype")
public class ERModelEntityMediator extends AbstractMediator {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private UiFactory uiFactory;

    private ObjectProperty<ERModelEntity> entity = new SimpleObjectProperty<>();
    private ListProperty<ERModelJoinPoint> joinPoints = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    private Runnable onMove;

    private List<ERModelEntityAttributeView> entityAttributeViews = new ArrayList<>();
    private ListProperty<Parent> entityAttributeViewsRoots = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    private boolean isRelationCreating = false;

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_STARTED, e -> isRelationCreating = true);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_FINISHED, e -> isRelationCreating = false);
    }

    public void configure(ERModelEntity dto) {
        entity.set(dto);
        entity.get().layoutXProperty().addListener((observable, oldValue, newValue) -> onMove.run());
        entity.get().layoutYProperty().addListener((observable, oldValue, newValue) -> onMove.run());
        entity.get().widthProperty().addListener((observable, oldValue, newValue) -> onMove.run());
        entity.get().heightProperty().addListener((observable, oldValue, newValue) -> onMove.run());

        entity.get().getAttributes().forEach(this::addAttribute);
        entity.get().attributesProperty().addListener(this::onAttributesChanged);
    }

    private void onAttributesChanged(ListChangeListener.Change<? extends ERModelEntityAttribute> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::addAttribute);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(this::removeAttribute);
            }
        }
    }

    private void addAttribute(ERModelEntityAttribute attributeDto) {
        ERModelEntityAttributeView attributeView = uiFactory.createView(ERModelEntityAttributeView.class);
        attributeView.getMediator().configure(attributeDto);

        entityAttributeViews.add(attributeView);
        entityAttributeViewsRoots.add(attributeView.getRootNode());
    }

    private void removeAttribute(ERModelEntityAttribute attribute) {
        ERModelEntityAttributeView attributeView = getTableAttributeViewFor(attribute);

        entityAttributeViews.remove(attributeView);
        entityAttributeViewsRoots.remove(attributeView.getRootNode());
    }

    private ERModelEntityAttributeView getTableAttributeViewFor(ERModelEntityAttribute attribute) {
        return entityAttributeViews.stream()
                .filter(view -> Objects.equals(view.getMediator().getAttribute(), attribute))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public void onMouseClicked() {
        if (isRelationCreating) {
            workflowService.startErModelEntityWorkflow(WorkflowType.ERM_SELECT_ENTITY_TO_CREATE_RELATION, entity.get());
        } else {
            workflowService.startErModelEntityWorkflow(WorkflowType.ERM_SELECT_ENTITY_TO_EDIT, entity.get());
        }
    }

    public void edit() {
        workflowService.startErModelEntityWorkflow(WorkflowType.ERM_EDIT_ENTITY, entity.get());
    }

    public void remove() {
        workflowService.startErModelEntityWorkflow(WorkflowType.ERM_REMOVE_ENTITY, entity.get());
    }

    public void addJoinPoint(ERModelJoinPoint joinPoint) {
        joinPoints.add(joinPoint);
        updateJoinPoints();
    }

    public void removeJoinPoint(ERModelJoinPoint joinPoint) {
        joinPoints.remove(joinPoint);
    }

    public void removeJoinPointsFor(ERModelRelation relation) {
        joinPoints.removeIf(jp -> Objects.equals(jp.getRelation(), relation));
        updateJoinPoints();
    }

    public void updateJoinPoints() {
        for (ERModelJoinPoint joinPoint : joinPoints) {
            List<ERModelEntity> relatedEntities = getRelatedEntities(joinPoint);

            double sumX = relatedEntities.stream()
                    .map(ERModelEntity::getCenterX)
                    .reduce(Double::sum)
                    .orElse(0d);

            double sumY = relatedEntities.stream()
                    .map(ERModelEntity::getCenterY)
                    .reduce(Double::sum)
                    .orElse(0d);

            double centerX = sumX / relatedEntities.size();
            double centerY = sumY / relatedEntities.size();

            if (entity.get().getLayoutX() + entity.get().getWidth() < centerX) {
                joinPoint.setSide(ERModelJoinPointSide.RIGHT);
            } else if (entity.get().getLayoutX() > centerX) {
                joinPoint.setSide(ERModelJoinPointSide.LEFT);
            } else if (entity.get().getLayoutY() + entity.get().getHeight() < centerY) {
                joinPoint.setSide(ERModelJoinPointSide.BOTTOM);
            } else if (entity.get().getLayoutY() > centerY) {
                joinPoint.setSide(ERModelJoinPointSide.TOP);
            } else {
                joinPoint.setSide(ERModelJoinPointSide.RIGHT);
            }
        }

        List<ERModelJoinPoint> leftJoinPoints = getJoinPointsBySide(ERModelJoinPointSide.LEFT);
        double leftDelta = entity.get().getHeight() / (leftJoinPoints.size() + 1);

        for (int i = 0; i < leftJoinPoints.size(); i++) {
            ERModelJoinPoint leftJoinPoint = leftJoinPoints.get(i);
            leftJoinPoint.setX(entity.get().getLayoutX());
            leftJoinPoint.setY(entity.get().getLayoutY() + (i + 1) * leftDelta);
        }

        List<ERModelJoinPoint> rightJoinPoints = getJoinPointsBySide(ERModelJoinPointSide.RIGHT);
        double rightDelta = entity.get().getHeight() / (rightJoinPoints.size() + 1);

        for (int i = 0; i < rightJoinPoints.size(); i++) {
            ERModelJoinPoint rightJoinPoint = rightJoinPoints.get(i);
            rightJoinPoint.setX(entity.get().getLayoutX() + entity.get().getWidth());
            rightJoinPoint.setY(entity.get().getLayoutY() + (i + 1) * rightDelta);
        }

        List<ERModelJoinPoint> topJoinPoints = getJoinPointsBySide(ERModelJoinPointSide.TOP);
        double topDelta = entity.get().getWidth() / (topJoinPoints.size() + 1);

        for (int i = 0; i < topJoinPoints.size(); i++) {
            ERModelJoinPoint topJoinPoint = topJoinPoints.get(i);
            topJoinPoint.setX(entity.get().getLayoutX() + (i + 1) * topDelta);
            topJoinPoint.setY(entity.get().getLayoutY());
        }

        List<ERModelJoinPoint> bottomJoinPoints = getJoinPointsBySide(ERModelJoinPointSide.BOTTOM);
        double bottomDelta = entity.get().getWidth() / (bottomJoinPoints.size() + 1);

        for (int i = 0; i < bottomJoinPoints.size(); i++) {
            ERModelJoinPoint bottomJoinPoint = bottomJoinPoints.get(i);
            bottomJoinPoint.setX(entity.get().getLayoutX() + (i + 1) * bottomDelta);
            bottomJoinPoint.setY(entity.get().getLayoutY() + entity.get().getHeight());
        }
    }

    private List<ERModelJoinPoint> getJoinPointsBySide(ERModelJoinPointSide side) {
        return joinPoints.stream()
                .filter(jp -> side.equals(jp.getSide()))
                .collect(toList());
    }

    private List<ERModelEntity> getRelatedEntities(ERModelJoinPoint joinPoint) {
        return joinPoint.getRelation().getRelationSides().stream()
                .map(ERModelRelationSide::getEntity)
                .collect(Collectors.toList());
    }

    public List<ERModelEntityAttributeView> getEntityAttributeViews() {
        return entityAttributeViews;
    }

    public ObservableList<Parent> getEntityAttributeViewsRoots() {
        return entityAttributeViewsRoots.get();
    }

    public ListProperty<Parent> entityAttributeViewsRootsProperty() {
        return entityAttributeViewsRoots;
    }

    public List<ERModelJoinPoint> getJoinPoints() {
        return joinPoints;
    }

    public ObjectProperty<ERModelEntity> entityProperty() {
        return entity;
    }

    public void setOnMove(Runnable onMove) {
        this.onMove = onMove;
    }
}
