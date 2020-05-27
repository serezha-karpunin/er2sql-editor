package com.etu.ui.ermodel.entity.node;


import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntitySelectType;
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
public class ERModelEntityView extends AbstractView {
    @FXML
    private AnchorPane entityViewRootAnchorPane;
    @FXML
    private HBox entityViewActionButtonHBox;
    @FXML
    private VBox entityViewRootVBox;
    @FXML
    private Label entityName;
    @FXML
    private VBox entityAttributeVBox;

    @Autowired
    private ERModelEntityMediator erModelEntityMediator;

    @Override
    protected void configureBindings() {
        erModelEntityMediator.entityProperty().addListener((observable, oldEntity, newEntity) -> {
            entityName.textProperty().bind(newEntity.nameProperty());
            newEntity.selectTypeProperty().addListener(getSelectTypeChangeListener());

            entityViewRootAnchorPane.layoutXProperty().bindBidirectional(newEntity.layoutXProperty());
            entityViewRootAnchorPane.layoutYProperty().bindBidirectional(newEntity.layoutYProperty());

            entityViewRootAnchorPane.boundsInLocalProperty().addListener(getBoundsChangeListener(newEntity));
        });

        erModelEntityMediator.entityAttributeViewsRootsProperty().addListener(this::onViewsChanged);
    }

    private ChangeListener<Bounds> getBoundsChangeListener(ERModelEntity entity) {
        return (observable, oldValue, newValue) -> {
            entity.setWidth(newValue.getWidth());
            entity.setHeight(newValue.getHeight());
        };
    }

    private ChangeListener<ERModelEntitySelectType> getSelectTypeChangeListener() {
        return (observable, oldValue, newValue) -> {
            entityViewRootVBox.getStyleClass().removeAll("selected-to-edit", "selected-to-create-relation");
            entityViewActionButtonHBox.setVisible(false);

            switch (newValue) {
                case SELECTED_TO_EDIT:
                    entityViewRootVBox.getStyleClass().add("selected-to-edit");
                    entityViewActionButtonHBox.setVisible(true);
                    break;
                case SELECTED_TO_CREATE_RELATION:
                    entityViewRootVBox.getStyleClass().add("selected-to-create-relation");
                    break;
                case NOT_SELECTED:
                    break;
            }
        };
    }

    private void onViewsChanged(ListChangeListener.Change<? extends Parent> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(entityAttributeVBox.getChildren()::add);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(entityAttributeVBox.getChildren()::remove);
            }
        }
    }

    @FXML
    private void onMouseClicked(MouseEvent event) {
        erModelEntityMediator.onMouseClicked();
        event.consume();
    }

    @FXML
    private void edit() {
        erModelEntityMediator.edit();
    }

    @FXML
    private void remove() {
        erModelEntityMediator.remove();
    }

    public ERModelEntityMediator getMediator() {
        return erModelEntityMediator;
    }

    @Override
    public Parent getRootNode() {
        return entityViewRootAnchorPane;
    }
}
