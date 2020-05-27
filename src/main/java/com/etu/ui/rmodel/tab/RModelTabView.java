package com.etu.ui.rmodel.tab;

import com.etu.ui.AbstractView;
import com.etu.ui.rmodel.canvas.RModelCanvasView;
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
public class RModelTabView extends AbstractView {

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private AnchorPane canvasAnchorPane;
    @FXML
    private Button addRelationButton;
    @FXML
    private Button addLinkButton;
    @FXML
    private Button finishLinkCreationButton;
    @FXML
    private Button cancelLinkCreationButton;

    @Autowired
    private RModelTabMediator rModelTabMediator;
    private RModelCanvasView rModelCanvasView;

    @Override
    protected void configureChildren() {
        scaleButton(addRelationButton);
        scaleButton(addLinkButton);
        scaleButton(finishLinkCreationButton);
        scaleButton(cancelLinkCreationButton);

        rModelCanvasView = rModelTabMediator.createCanvas();
        canvasAnchorPane.getChildren().add(rModelCanvasView.getRootNode());

        VBox.setVgrow(canvasAnchorPane, Priority.ALWAYS);
        canvasAnchorPane.setMaxWidth(Double.MAX_VALUE);
    }

    private void scaleButton(Button button) {
        ((ImageView) button.getGraphic()).setFitWidth(70);
        ((ImageView) button.getGraphic()).setFitHeight(55);
    }

    @Override
    protected void configureBindings() {
        addRelationButton.disableProperty().bind(rModelTabMediator.linkCreationStartedProperty());
        addLinkButton.disableProperty().bind(rModelTabMediator.linkCreationStartedProperty());

        finishLinkCreationButton.visibleProperty().bind(rModelTabMediator.linkCreationStartedProperty());
        cancelLinkCreationButton.visibleProperty().bind(rModelTabMediator.linkCreationStartedProperty());
    }

    @FXML
    private void startAddRelationWorkflow() {
        rModelTabMediator.startAddRelationWorkflow();
    }

    @FXML
    private void startAddLinkWorkflow() {
        rModelTabMediator.startAddLinkWorkflow();
    }

    @FXML
    private void finishLinkCreation() {
        rModelTabMediator.finishLinkCreation();
    }

    @FXML
    private void cancelLinkCreation() {
        rModelTabMediator.cancelLinkCreation();
    }

    @FXML
    private void startSqlGenerationWorkflow() {
        rModelTabMediator.startSqlGenerationWorkflow();
    }

    @Override
    public Parent getRootNode() {
        return rootAnchorPane;
    }
}
