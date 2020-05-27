package com.etu.ui.ermodel.relation.editor;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType;
import com.etu.ui.AbstractMediator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

@Component
@Scope("prototype")
public class ERModelRelationEditorMediator extends AbstractMediator {

    private ERModelRelation relationToUpdate;
    private ERModelRelation editableRelation = new ERModelRelation();

    private Consumer<ERModelRelation> onSave;
    private Consumer<ERModelRelation> onRemove;
    private Runnable onCancel;

    public void initEditor(ERModelRelation dto, Consumer<ERModelRelation> onSave, Consumer<ERModelRelation> onRemove, Runnable onCancel) {
        relationToUpdate = dto;

        copyRelationToEditor(relationToUpdate, editableRelation);

        this.onSave = onSave;
        this.onRemove = onRemove;
        this.onCancel = onCancel;
    }

    public void save() {
        copyRelationFromEditor(editableRelation, relationToUpdate);
        onSave.accept(relationToUpdate);
    }

    public void remove() {
        onRemove.accept(relationToUpdate);
    }

    public void close() {
        onCancel.run();
    }

    private void copyRelationToEditor(ERModelRelation source, ERModelRelation target) {
        target.setRelationType(source.getRelationType());
        target.setName(source.getName());
        target.getRelationSides().clear();
        source.getRelationSides().forEach(
                relationSide -> {
                    ERModelRelationSide copiedRelationSide = new ERModelRelationSide();

                    copiedRelationSide.setId(relationSide.getId());
                    copiedRelationSide.setEntity(relationSide.getEntity());
                    copiedRelationSide.setMandatory(relationSide.isMandatory());
                    copiedRelationSide.setType(relationSide.getType());

                    target.getRelationSides().add(copiedRelationSide);
                }
        );
    }

    private void copyRelationFromEditor(ERModelRelation source, ERModelRelation target) {
        target.setName(source.getName());
        source.getRelationSides().forEach(
                relationSide -> {
                    ERModelRelationSide targetSide = getRelationSideFor(target, relationSide.getId());

                    targetSide.setMandatory(relationSide.isMandatory());
                    targetSide.setType(relationSide.getType());
                }
        );
    }

    private ERModelRelationSide getRelationSideFor(ERModelRelation target, String id) {
        return target.getRelationSides().stream()
                .filter(side -> Objects.equals(side.getId(), id))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public StringProperty relationNameProperty() {
        return editableRelation.nameProperty();
    }


    public ObjectProperty<ERModelRelationType> relationTypeProperty() {
        return editableRelation.relationTypeProperty();
    }


    public ListProperty<ERModelRelationSide> relationSidesProperty() {
        return editableRelation.relationSidesProperty();
    }
}
