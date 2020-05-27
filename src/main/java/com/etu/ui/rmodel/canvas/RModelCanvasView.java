package com.etu.ui.rmodel.canvas;

import com.etu.ui.AbstractView;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RModelCanvasView extends AbstractView {
    @FXML
    private ScrollPane rmCanvasScrollPane;
    @FXML
    private AnchorPane rmCanvasContent;

    @Autowired
    private RModelCanvasMediator rModelCanvasMediator;

    @Override
    protected void configureBindings() {
        rModelCanvasMediator.relationViewsRootsProperty().addListener(this::onTableViewsChanged);
        rModelCanvasMediator.linkViewsRootsProperty().addListener(this::onRelationViewsChanged);
    }

    private void onTableViewsChanged(ListChangeListener.Change<? extends Parent> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::addTableViewToCanvas);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(rmCanvasContent.getChildren()::remove);
            }
        }
    }

    private void onRelationViewsChanged(ListChangeListener.Change<? extends Parent> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(rmCanvasContent.getChildren()::add);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(rmCanvasContent.getChildren()::remove);
            }
        }
    }

    private void addTableViewToCanvas(Parent view) {
        if (view.getLayoutX() != 0 || view.getLayoutY() != 0) {
            rmCanvasContent.getChildren().add(view);
            return;
        }

        double canvasWidth = rmCanvasScrollPane.getWidth();
        double canvasHeight = rmCanvasScrollPane.getHeight();

        double viewWidth = 200;
        double viewHeight = 300;

        int columnAmount = (int) (canvasWidth / viewWidth);
        int rowAmount = (int) (canvasHeight / viewHeight);

        int maxRetryAmount = 10;
        for (int i = 0; i < maxRetryAmount; i++) {
            for (int rowIndex = 0; rowIndex < rowAmount; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnAmount; columnIndex++) {
                    view.setLayoutX(columnIndex * viewWidth + 20 * columnIndex);
                    view.setLayoutY(rowIndex * viewHeight + 20 * rowIndex);
                    if (hasNoIntersection(view)) {
                        rmCanvasContent.getChildren().add(view);
                        return;
                    }
                }
            }
        }
        view.setLayoutX(0);
        view.setLayoutY(0);
        rmCanvasContent.getChildren().add(view);
    }

    private boolean hasNoIntersection(Parent view) {
        return rModelCanvasMediator.getRelationViewsRoots().stream()
                .filter(v -> v != view)
                .filter(rmCanvasContent.getChildren()::contains)
                .noneMatch(v -> {
                    BoundingBox firstBb = new BoundingBox(v.getLayoutX(), v.getLayoutY(), 200, 300);
                    BoundingBox secondBb = new BoundingBox(view.getLayoutX(), view.getLayoutY(), 200, 300);
                    return firstBb.intersects(secondBb);
                });
    }

    @FXML
    private void onMouseClick() {
        rModelCanvasMediator.onMouseClick();
    }

    @FXML
    private void dragOver(DragEvent event) {
        rModelCanvasMediator.dragOver(event);
    }

    @FXML
    private void dragDone(DragEvent event) {
        rModelCanvasMediator.dragDone(event);
    }

    @Override
    public Parent getRootNode() {
        return rmCanvasScrollPane;
    }
}
