package com.etu.ui.ermodel.entity.node.attribute;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.ui.AbstractView;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ERModelEntityAttributeView extends AbstractView {

    @FXML
    private Label label;

    @Autowired
    private ERModelEntityAttributeMediator erModelEntityAttributeMediator;

    @Override
    protected void configureChildren() {
        label.setPrefHeight(24);
    }

    @Override
    protected void configureBindings() {
        erModelEntityAttributeMediator.attributeProperty().addListener(((observable, oldAttribute, newAttribute) -> {
            newAttribute.keyProperty().addListener(getChangeListener());
            newAttribute.nameProperty().addListener(getChangeListener());

            updateLabelText();
        }));
    }

    private ChangeListener<Object> getChangeListener() {
        return (observable, oldValue, newValue) -> updateLabelText();
    }

    private void updateLabelText() {
        ERModelEntityAttribute attribute = erModelEntityAttributeMediator.getAttribute();
        label.setText(attribute.getName());
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

    public ERModelEntityAttributeMediator getMediator() {
        return erModelEntityAttributeMediator;
    }
}
