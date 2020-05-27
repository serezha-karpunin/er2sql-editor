package com.etu.ui.rmodel.relation.node;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationSelectType;
import com.etu.ui.AbstractView;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RModelRelationView extends AbstractView {
    @FXML
    private AnchorPane relationViewRootAnchorPane;
    @FXML
    private HBox relationViewActionButtonHBox;
    @FXML
    private VBox relationViewRootVBox;
    @FXML
    private Label relationName;
    @FXML
    private VBox relationAttributeVBox;

    @Autowired
    private RModelRelationMediator rModelTableMediator;

    @Override
    protected void configureBindings() {
        rModelTableMediator.relationProperty().addListener((observable, oldRelation, newRelation) -> {
            relationName.textProperty().bind(newRelation.nameProperty());
            newRelation.selectTypeProperty().addListener(getSelectTypeChangeListener());

            relationViewRootAnchorPane.setLayoutX(newRelation.getX());
            relationViewRootAnchorPane.setLayoutY(newRelation.getY());

            relationViewRootAnchorPane.boundsInParentProperty().addListener(getBoundsChangeListener(newRelation));
        });

        rModelTableMediator.relationAttributeViewsRootsProperty().addListener(this::onViewsChanged);
    }


    private ChangeListener<RModelRelationSelectType> getSelectTypeChangeListener() {
        return (observable, oldValue, newValue) -> {
            relationViewRootVBox.getStyleClass().removeAll("selected-to-edit", "selected-to-create-link");
            relationViewActionButtonHBox.setVisible(false);

            switch (newValue) {
                case SELECTED_TO_EDIT:
                    relationViewRootVBox.getStyleClass().add("selected-to-edit");
                    relationViewActionButtonHBox.setVisible(true);
                    break;
                case SELECTED_TO_CREATE_LINK:
                    relationViewRootVBox.getStyleClass().add("selected-to-create-link");
                    break;
                case NOT_SELECTED:
                    break;
            }
        };
    }

    private ChangeListener<? super Bounds> getBoundsChangeListener(RModelRelation relation) {
        return (observable, oldValue, newValue) -> {
            relation.setX(newValue.getMinX());
            relation.setY(newValue.getMinY());
            relation.setWidth(newValue.getWidth());
            relation.setHeight(newValue.getHeight());
            relation.setBounds(newValue);
        };
    }

    private void onViewsChanged(ListChangeListener.Change<? extends Parent> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(relationAttributeVBox.getChildren()::add);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(relationAttributeVBox.getChildren()::remove);
            }
        }
    }

    @FXML
    private void onMouseClick(MouseEvent event) {
        rModelTableMediator.onMouseClicked();
        event.consume();
    }

    @FXML
    private void edit(MouseEvent event) {
        rModelTableMediator.edit();
        event.consume();
    }

    @FXML
    private void remove(MouseEvent event) {
        rModelTableMediator.remove();
        event.consume();
    }

    public RModelRelationMediator getMediator() {
        return rModelTableMediator;
    }

    @Override
    public Parent getRootNode() {
        return relationViewRootAnchorPane;
    }
}
