package com.etu.ui.rmodel.relation.node.attribute;

import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.ui.AbstractView;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RModelRelationAttributeView extends AbstractView {

    @FXML
    private Label label;

    @Autowired
    private RModelRelationAttributeMediator rModelRelationAttributeMediator;
    @Autowired
    private LocalizationService localizationService;

    @Override
    protected void configureChildren() {
        label.setPrefHeight(24);
    }

    @Override
    protected void configureBindings() {
        rModelRelationAttributeMediator.attributeProperty().addListener(((observable, oldAttribute, newAttribute) -> {
            newAttribute.keyProperty().addListener(getChangeListener());
            newAttribute.nameProperty().addListener(getChangeListener());
            newAttribute.typeProperty().addListener(getChangeListener());

            newAttribute.getRelation().boundsProperty().addListener(getParentBoundsChangeListener(newAttribute));
            label.boundsInParentProperty().addListener(((o, ov, nv) -> updateBounds(newAttribute)));

            updateLabelText();
        }));
    }

    private ChangeListener<? super Bounds> getParentBoundsChangeListener(RModelRelationAttribute attribute) {
        return (observable, oldParentBounds, newParentBounds) -> updateBounds(attribute);
    }

    private void updateBounds(RModelRelationAttribute attribute) {
        if (label.getParent() != null) {
            double y = label.getParent().localToParent(label.getBoundsInParent()).getMinY();
            attribute.setY(y);
        }

        attribute.setHeight(label.getHeight());
    }

    private ChangeListener<Object> getChangeListener() {
        return (observable, oldValue, newValue) -> updateLabelText();
    }

    private void updateLabelText() {
        RModelRelationAttribute attribute = rModelRelationAttributeMediator.getAttribute();
        String localizedType = localizationService.getLocalizedString(attribute.getType().name());

        label.setText(String.format("%s : %s", attribute.getName(), localizedType));
        if (attribute.isKey()) {
            label.setStyle(("-fx-font-weight: bold"));
        } else {
            label.setStyle("");
        }
    }

    @Override
    public Parent getRootNode() {
        return label;
    }

    public RModelRelationAttributeMediator getMediator() {
        return rModelRelationAttributeMediator;
    }
}
