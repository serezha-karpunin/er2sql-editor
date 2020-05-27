package com.etu.ui.ermodel.entity.editor;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.ui.AbstractMediator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

@Component
@Scope("prototype")
public class ERModelEntityEditorMediator extends AbstractMediator {

    private ERModelEntity entityToUpdate;
    private ERModelEntity editableEntity = new ERModelEntity();

    private Consumer<ERModelEntity> onSuccess;
    private Runnable onCancel;

    public void configureEditor(ERModelEntity entity, Consumer<ERModelEntity> onSuccess, Runnable onCancel) {
        entityToUpdate = entity;

        copyEntity(entityToUpdate, editableEntity);

        this.onSuccess = onSuccess;
        this.onCancel = onCancel;
    }

    public void addAttribute() {
        editableEntity.getAttributes().add(new ERModelEntityAttribute());
    }

    public void confirm() {
        copyEntity(editableEntity, entityToUpdate);
        onSuccess.accept(entityToUpdate);
    }

    private void copyEntity(ERModelEntity source, ERModelEntity target) {
        target.setName(source.getName());
        copyAttributes(source, target);
    }

    private void copyAttributes(ERModelEntity source, ERModelEntity target) {
        List<ERModelEntityAttribute> currentTargetAttributes = new ArrayList<>(target.getAttributes());
        target.getAttributes().clear();

        source.getAttributes().forEach(
                sourceAttribute -> {
                    ERModelEntityAttribute targetAttribute = getTargetAttribute(sourceAttribute, currentTargetAttributes);
                    targetAttribute.setId(sourceAttribute.getId());
                    targetAttribute.setKey(sourceAttribute.isKey());
                    targetAttribute.setName(sourceAttribute.getName());
                    target.getAttributes().add(targetAttribute);
                }
        );
    }

    private ERModelEntityAttribute getTargetAttribute(ERModelEntityAttribute sourceAttribute, List<ERModelEntityAttribute> currentTargetAttributes) {
        return currentTargetAttributes.stream()
                .filter(hasId(sourceAttribute.getId()))
                .findFirst()
                .orElseGet(ERModelEntityAttribute::new);
    }

    private Predicate<ERModelEntityAttribute> hasId(String sourceAttributeId) {
        return attribute -> ofNullable(attribute)
                .map(ERModelEntityAttribute::getId)
                .filter(id -> Objects.equals(id, sourceAttributeId))
                .isPresent();
    }

    public void close() {
        onCancel.run();
    }

    public StringProperty entityNameProperty() {
        return editableEntity.nameProperty();
    }

    public ListProperty<ERModelEntityAttribute> entityAttributesProperty() {
        return editableEntity.attributesProperty();
    }
}
