package com.etu.ui.ermodel.relation.node;


import com.etu.ui.AbstractView;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ERModelRelationView extends AbstractView {
    @FXML
    private Group group;

    @Autowired
    private ERModelRelationMediator erModelRelationMediator;

    @Override
    protected void configureBindings() {
        erModelRelationMediator.childrenProperty().addListener(this::onChildrenChanged);

        erModelRelationMediator.relationProperty().addListener((observable, oldRelation, newRelation) -> {
            newRelation.nameProperty().addListener(getNameChangeListener());
            newRelation.selectedProperty().addListener(getSelectedChangeListener());
            newRelation.getRelationSides().forEach(side -> side.mandatoryProperty().addListener(getMandatoryChangeListener()));
        });
    }

    private ChangeListener<String> getNameChangeListener() {
        return (observable, oldValue, newValue) -> erModelRelationMediator.redrawRelation();
    }

    private ChangeListener<Boolean> getSelectedChangeListener() {
        return (observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                group.getStyleClass().add("selected");
            } else if (oldValue && !newValue) {
                group.getStyleClass().remove("selected");
            }
        };
    }

    private ChangeListener<Boolean> getMandatoryChangeListener() {
        return (observable, oldValue, newValue) -> erModelRelationMediator.redrawRelation();
    }


    private void onChildrenChanged(ListChangeListener.Change<? extends Node> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(group.getChildren()::add);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(group.getChildren()::remove);
            }
        }
    }

    @Override
    public Parent getRootNode() {
        return group;
    }

    public ERModelRelationMediator getMediator() {
        return erModelRelationMediator;
    }

    @FXML
    public void onMouseClick(MouseEvent event) {
        getMediator().onMouseClick(event);
        event.consume();
    }
}
