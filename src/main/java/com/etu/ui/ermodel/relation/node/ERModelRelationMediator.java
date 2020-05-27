package com.etu.ui.ermodel.relation.node;


import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.ui.AbstractMediator;
import com.etu.ui.ermodel.canvas.ERModelJoinPoint;
import com.etu.ui.ermodel.relation.node.draw.ERModelDefaultDrawPathStrategy;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;

@Component
@Scope("prototype")
public class ERModelRelationMediator extends AbstractMediator {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ERModelDefaultDrawPathStrategy erModelDefaultDrawPathStrategy;

    private ObjectProperty<ERModelRelation> relation = new SimpleObjectProperty<>();
    private ListProperty<ERModelJoinPoint> joinPoints = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    private ListProperty<Node> children = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    public void configure(ERModelRelation dto) {
        relation.set(dto);
    }

    public ListProperty<Node> childrenProperty() {
        return children;
    }

    public void addJoinPoint(ERModelJoinPoint joinPoint) {
        joinPoint.xProperty().addListener((observable, oldValue, newValue) -> redrawRelation());
        joinPoint.yProperty().addListener((observable, oldValue, newValue) -> redrawRelation());
        joinPoints.add(joinPoint);

        redrawRelation();
    }

    public void removeJoinPoint(ERModelJoinPoint joinPoint) {
        joinPoints.remove(joinPoint);
    }

    public void onMouseClick(MouseEvent event) {
        workflowService.startErModelRelationWorkflow(WorkflowType.ERM_SELECT_RELATION, relation.get());

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                workflowService.startErModelRelationWorkflow(WorkflowType.ERM_EDIT_RELATION, relation.get());
            }
        }
    }

    private List<Node> getNodesToDrawFor(ERModelRelationSide relationSide, double centerX, double centerY) {
        ERModelJoinPoint jp = getJoinPointByEntity(relationSide.getEntity());
        List<Node> nodes = new ArrayList<>();

        nodes.add(erModelDefaultDrawPathStrategy.getPath(relationSide, jp, centerX, centerY));
        getHead(jp, relationSide).ifPresent(nodes::add);

        return nodes;
    }

    private boolean allJoinPointsArePresent() {
        return joinPoints.size() == relation.get().getRelationSides().size();
    }

    public void redrawRelation() {
        if (!allJoinPointsArePresent()) {
            return;
        }

        children.clear();

        double centerX = getJoinPointCenterX();
        double centerY = getJoinPointCenterY();

        List<Node> nodesToDraw = relation.get().getRelationSides().stream()
                .map(side -> getNodesToDrawFor(side, centerX, centerY))
                .flatMap(Collection::stream)
                .collect(toList());

        getNameLabel(relation.get().getName(), centerX, centerY)
                .ifPresent(nodesToDraw::add);

        children.addAll(nodesToDraw);
    }

    private Optional<Label> getNameLabel(String name, double centerX, double centerY) {
        if (name != null) {
            Label nameLabel = new Label(name);
            nameLabel.setLayoutX(centerX + 5);
            nameLabel.setLayoutY(centerY);
            return of(nameLabel);
        }
        return empty();
    }

    private double getJoinPointCenterX() {
        double sumX = joinPoints.stream()
                .map(ERModelJoinPoint::getX)
                .reduce(Double::sum)
                .orElse(0d);

        return sumX / joinPoints.size();
    }

    private double getJoinPointCenterY() {
        double sumX = joinPoints.stream()
                .map(ERModelJoinPoint::getY)
                .reduce(Double::sum)
                .orElse(0d);

        return sumX / joinPoints.size();
    }

    private ERModelJoinPoint getJoinPointByEntity(ERModelEntity entity) {
        return joinPoints.stream()
                .filter(jp -> Objects.equals(jp.getEntity(), entity))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Optional<Node> getHead(ERModelJoinPoint jp, ERModelRelationSide relationSide) {
        double x = jp.getX();
        double y = jp.getY();

        Node resultNode = null;

        switch (relationSide.getType()) {
            case MANY:
                Polyline polyline = new Polyline();
                polyline.getPoints().addAll(
                        x, y + 5,
                        x + 10, y,
                        x, y,
                        x + 10, y,
                        x, y - 5

                );
                polyline.setStrokeWidth(2d);
                resultNode = polyline;
                break;
            case DEPENDENT:
                Polygon dependentPolygon = new Polygon();
                dependentPolygon.getPoints().addAll(
                        x, y,
                        x + 10, y + 5,
                        x + 10, y - 5,
                        x, y
                );
                dependentPolygon.setFill(Color.WHITE);
                dependentPolygon.setStroke(Color.BLACK);
                dependentPolygon.setStrokeWidth(2d);
                resultNode = dependentPolygon;
                break;
            case SPECIFIC:
                Polygon specificPolygon = new Polygon();
                specificPolygon.getPoints().addAll(
                        x, y,
                        x + 10, y + 5,
                        x + 10, y - 5,
                        x, y
                );
                specificPolygon.setFill(Color.BLACK);
                specificPolygon.setStroke(Color.BLACK);
                specificPolygon.setStrokeWidth(2d);
                resultNode = specificPolygon;
                break;
        }

        if (nonNull(resultNode)) {
            rotateHead(resultNode, jp);
        }

        return ofNullable(resultNode);
    }

    private void rotateHead(Node node, ERModelJoinPoint joinPoint) {
        switch (joinPoint.getSide()) {
            case RIGHT:
                return;
            case BOTTOM:
                node.getTransforms().add(new Rotate(90, joinPoint.getX(), joinPoint.getY()));
                return;
            case LEFT:
                node.getTransforms().add(new Rotate(180, joinPoint.getX(), joinPoint.getY()));
                return;
            case TOP:
                node.getTransforms().add(new Rotate(270, joinPoint.getX(), joinPoint.getY()));
                return;
        }
    }

    public ERModelRelation getRelation() {
        return relation.get();
    }

    public ObjectProperty<ERModelRelation> relationProperty() {
        return relation;
    }
}
