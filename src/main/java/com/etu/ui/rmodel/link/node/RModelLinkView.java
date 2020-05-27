package com.etu.ui.rmodel.link.node;

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
public class RModelLinkView extends AbstractView {
    @FXML
    private Group group;

    @Autowired
    private RModelLinkMediator rModelLinkMediator;

    @Override
    protected void configureBindings() {
        rModelLinkMediator.childrenProperty().addListener(this::onChildrenChanged);

        rModelLinkMediator.linkProperty().addListener((observable, oldLink, newLink) -> {
            newLink.selectedProperty().addListener(getSelectedChangeListener());
        });
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

    private ChangeListener<Boolean> getSelectedChangeListener() {
        return (observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                group.getStyleClass().add("selected");
            } else if (oldValue && !newValue) {
                group.getStyleClass().remove("selected");
            }
        };
    }

    public RModelLinkMediator getMediator() {
        return rModelLinkMediator;
    }

    @Override
    public Parent getRootNode() {
        return group;
    }

    @FXML
    public void onMouseClick(MouseEvent event) {
        getMediator().onMouseClick(event);
        event.consume();
    }
}
