package com.etu.ui.ermodel.tab;

import com.etu.ui.AbstractView;
import com.etu.ui.ermodel.canvas.ERModelCanvasView;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ERModelTabView extends AbstractView {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private Button addEntityButton;
    @FXML
    private Button addOneToOneRelationButton;
    @FXML
    private Button addOneToManyRelationButton;
    @FXML
    private Button addManyToManyRelationButton;
    @FXML
    private Button addDependencyRelationButton;
    @FXML
    private Button addCategoryRelationButton;
    @FXML
    private Button finishRelationCreationButton;
    @FXML
    private Button cancelRelationCreationButton;
    @FXML
    private Button toRelationModelButton;
    @FXML
    private AnchorPane canvasAnchorPane;

    @Autowired
    private ERModelTabMediator erModelTabMediator;
    private ERModelCanvasView erModelCanvasView;

    @Override
    protected void configureChildren() {
        scaleButton(addEntityButton);
        scaleButton(addOneToOneRelationButton);
        scaleButton(addOneToManyRelationButton);
        scaleButton(addManyToManyRelationButton);
        scaleButton(addDependencyRelationButton);
        scaleButton(addCategoryRelationButton);
        scaleButton(finishRelationCreationButton);
        scaleButton(cancelRelationCreationButton);

        erModelCanvasView = erModelTabMediator.createCanvas();
        canvasAnchorPane.getChildren().add(erModelCanvasView.getRootNode());

        VBox.setVgrow(canvasAnchorPane, Priority.ALWAYS);
        canvasAnchorPane.setMaxWidth(Double.MAX_VALUE);
    }

    private void scaleButton(Button button) {
        ((ImageView) button.getGraphic()).setFitWidth(70);
        ((ImageView) button.getGraphic()).setFitHeight(55);
    }

    @Override
    protected void configureBindings() {
        addEntityButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        addOneToOneRelationButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        addOneToManyRelationButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        addManyToManyRelationButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        addDependencyRelationButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        addCategoryRelationButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        toRelationModelButton.disableProperty().bind(erModelTabMediator.relationCreationStartedProperty());

        finishRelationCreationButton.visibleProperty().bind(erModelTabMediator.relationCreationStartedProperty());
        cancelRelationCreationButton.visibleProperty().bind(erModelTabMediator.relationCreationStartedProperty());
    }

    @FXML
    private void startAddEntityWorkflow() {
        erModelTabMediator.startAddEntityWorkflow();
    }

    @FXML
    private void startAddOneToOneRelationWorkflow() {
        erModelTabMediator.startAddOneToOneRelationWorkflow();
    }

    @FXML
    private void startAddOneToManyRelationWorkflow() {
        erModelTabMediator.startAddOneToManyRelationWorkflow();
    }

    @FXML
    private void startAddManyToManyRelationWorkflow() {
        erModelTabMediator.startAddManyToManyRelationWorkflow();
    }

    @FXML
    private void startAddDependencyRelationWorkflow() {
        erModelTabMediator.startAddDependencyRelationWorkflow();
    }

    @FXML
    private void startAddCategoryRelationWorkflow() {
        erModelTabMediator.startAddCategoryRelationWorkflow();
    }

    @FXML
    private void finishRelationCreation() {
        erModelTabMediator.finishRelationCreation();
    }

    @FXML
    private void cancelRelationCreation() {
        erModelTabMediator.cancelRelationCreation();
    }

    @FXML
    private void startTransformationWorkflow() {
        erModelTabMediator.startTransformationWorkflow();
    }

    @Override
    public Parent getRootNode() {
        return rootAnchorPane;
    }

}
