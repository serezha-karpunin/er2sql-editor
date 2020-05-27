package com.etu.ui.ermodel.canvas;


import com.etu.ui.AbstractView;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ERModelCanvasView extends AbstractView {

    @FXML
    private ScrollPane ermCanvasScrollPane;
    @FXML
    private AnchorPane ermCanvasContent;

    @Autowired
    private ERModelCanvasMediator erModelCanvasMediator;

    @Override
    protected void configureBindings() {
        erModelCanvasMediator.entityViewsRootsProperty().addListener(this::onViewsChanged);
        erModelCanvasMediator.relationViewsRootsProperty().addListener(this::onViewsChanged);
    }

    private void onViewsChanged(ListChangeListener.Change<? extends Parent> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::addToCanvas);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(this::removeFromCanvas);
            }
        }
    }

    @FXML
    private void dragOver(DragEvent event) {
        erModelCanvasMediator.dragOver(event);
    }

    @FXML
    private void dragDone(DragEvent event) {
        erModelCanvasMediator.dragDone(event);
    }

    @FXML
    private void onMouseClick() {
        erModelCanvasMediator.onMouseClick();
    }

    @Override
    public Parent getRootNode() {
        return ermCanvasScrollPane;
    }

    private void addToCanvas(Parent view) {
        ermCanvasContent.getChildren().add(view);
    }

    private void removeFromCanvas(Parent view) {
        ermCanvasContent.getChildren().remove(view);
    }
}
