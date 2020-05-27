package com.etu.ui.rmodel.link.node;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSide;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.ui.AbstractMediator;
import com.etu.ui.rmodel.canvas.RModelJoinPoint;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Scope("prototype")
public class RModelLinkMediator extends AbstractMediator {

    @Autowired
    private WorkflowService workflowService;

    private ObjectProperty<RModelLink> link = new SimpleObjectProperty<>();
    private ListProperty<RModelJoinPointPair> joinPointPairs = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    private ListProperty<Node> children = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    public void configure(RModelLink dto) {
        link.set(dto);
    }

    public ObjectProperty<RModelLink> linkProperty() {
        return link;
    }

    public RModelLink getLink() {
        return link.get();
    }

    public ListProperty<Node> childrenProperty() {
        return children;
    }

    public void addJoinPointPair(RModelJoinPointPair joinPointPair) {
        registerPropertyListeners(joinPointPair.getJoinPointFrom());
        registerPropertyListeners(joinPointPair.getJoinPointTo());
        joinPointPairs.add(joinPointPair);
    }

    private void registerPropertyListeners(RModelJoinPoint joinPoint) {
        joinPoint.xProperty().addListener((observable, oldValue, newValue) -> redrawLink());
        joinPoint.yProperty().addListener((observable, oldValue, newValue) -> redrawLink());
    }

    public void onMouseClick(MouseEvent event) {
        workflowService.startRModelLinkWorkflow(WorkflowType.RM_SELECT_LINK, link.get());

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                workflowService.startRModelLinkWorkflow(WorkflowType.RM_EDIT_LINK, link.get());
            }
        }
    }

    private boolean allJoinPointPairsArePresent() {
        return joinPointPairs.size() == link.get().getLinkedAttributesMap().size();
    }

    public void redrawLink() {
        if (!allJoinPointPairsArePresent()) {
            return;
        }

        children.clear();

        RModelLinkSide sideFrom = link.get().getLinkSideFrom();
        RModelLinkSide sideTo = link.get().getLinkSideTo();

        for (int i = 0; i < joinPointPairs.size(); i++) {
            RModelJoinPointPair pair = joinPointPairs.get(i);
            RModelJoinPoint jpFrom = pair.getJoinPointFrom();
            RModelJoinPoint jpTo = pair.getJoinPointTo();
            List<Path> paths = getPaths(jpFrom, jpTo, i * 5);

            children.addAll(paths);
            children.add(getHead(jpFrom, sideFrom));
            children.add(getHead(jpTo, sideTo));
        }
    }

    private List<Path> getPaths(RModelJoinPoint firstJp, RModelJoinPoint secondJp, int delta) {
        double centerX = ((firstJp.getX() + secondJp.getX()) / 2) - delta;
        double centerY = ((firstJp.getY() + secondJp.getY()) / 2);

        Path firstPath = buildPath(firstJp.getX(), firstJp.getY(), centerX, centerY);
        Path secondPath = buildPath(secondJp.getX(), secondJp.getY(), centerX, centerY);

        return Arrays.asList(firstPath, secondPath);
    }

    private Path buildPath(double fromX, double fromY, double toX, double toY) {
        Path path = new Path();

        path.getElements().add(new MoveTo(fromX, fromY));
        path.getElements().add(new LineTo(toX, fromY));
        path.getElements().add(new LineTo(toX, toY));

        path.setStrokeWidth(2d);
        return path;
    }

    private Node getHead(RModelJoinPoint jp, RModelLinkSide side) {
        double x = jp.getX();
        double y = jp.getY();

        Label label = new Label();
        switch (side.getType()) {
            case ONE:
                label.setText("1");
                break;
            case MANY:
                label.setText("n");
        }

        switch (jp.getSide()) {
            case LEFT:
                label.setLayoutX(x - 10);
                label.setLayoutY(y);
                break;
            case RIGHT:
                label.setLayoutX(x + 10);
                label.setLayoutY(y);
                break;
        }

        return label;
    }

    public static class RModelJoinPointPair {
        private RModelJoinPoint joinPointFrom;
        private RModelJoinPoint joinPointTo;

        public RModelJoinPoint getJoinPointFrom() {
            return joinPointFrom;
        }

        public void setJoinPointFrom(RModelJoinPoint joinPointFrom) {
            this.joinPointFrom = joinPointFrom;
        }

        public RModelJoinPoint getJoinPointTo() {
            return joinPointTo;
        }

        public void setJoinPointTo(RModelJoinPoint joinPointTo) {
            this.joinPointTo = joinPointTo;
        }
    }
}
