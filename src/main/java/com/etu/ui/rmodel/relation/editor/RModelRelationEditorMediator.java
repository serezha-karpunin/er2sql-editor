package com.etu.ui.rmodel.relation.editor;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
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
public class RModelRelationEditorMediator extends AbstractMediator {

    private RModelRelation relationToUpdate;
    private RModelRelation editableRelation = new RModelRelation();

    private Consumer<RModelRelation> onSuccess;
    private Runnable onCancel;

    public void configureEditor(RModelRelation relation, Consumer<RModelRelation> onSuccess, Runnable onCancel) {
        relationToUpdate = relation;

        copyRelation(relationToUpdate, editableRelation);

        this.onSuccess = onSuccess;
        this.onCancel = onCancel;
    }

    public void addAttribute() {
        editableRelation.getAttributes().add(new RModelRelationAttribute());
    }

    public void confirm() {
        copyRelation(editableRelation, relationToUpdate);
        onSuccess.accept(relationToUpdate);
    }

    private void copyRelation(RModelRelation source, RModelRelation target) {
        target.setName(source.getName());
        copyAttributes(source, target);

    }

    private void copyAttributes(RModelRelation source, RModelRelation target) {
        List<RModelRelationAttribute> currentTargetAttributes = new ArrayList<>(target.getAttributes());
        target.getAttributes().clear();

        source.getAttributes().forEach(
                sourceAttribute -> {
                    RModelRelationAttribute targetAttribute = getTargetAttribute(sourceAttribute, currentTargetAttributes);
                    targetAttribute.setId(sourceAttribute.getId());
                    targetAttribute.setKey(sourceAttribute.isKey());
                    targetAttribute.setName(sourceAttribute.getName());
                    targetAttribute.setType(sourceAttribute.getType());
                    targetAttribute.setRelation(target);
                    target.getAttributes().add(targetAttribute);
                }
        );
    }

    private RModelRelationAttribute getTargetAttribute(RModelRelationAttribute sourceAttribute, List<RModelRelationAttribute> currentTargetAttributes) {
        return currentTargetAttributes.stream()
                .filter(hasId(sourceAttribute.getId()))
                .findFirst()
                .orElseGet(RModelRelationAttribute::new);
    }

    private Predicate<RModelRelationAttribute> hasId(String sourceAttributeId) {
        return attribute -> ofNullable(attribute)
                .map(RModelRelationAttribute::getId)
                .filter(id -> Objects.equals(id, sourceAttributeId))
                .isPresent();
    }

    public void close() {
        onCancel.run();
    }

    public StringProperty relationNameProperty() {
        return editableRelation.nameProperty();
    }

    public ListProperty<RModelRelationAttribute> relationAttributesProperty() {
        return editableRelation.attributesProperty();
    }
}
