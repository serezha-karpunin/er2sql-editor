package com.etu.infrastructure.state.dto.runtime.erm;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ERModelRelationSide {
    private String id;
    private ERModelRelationSideType type;

    private SimpleBooleanProperty mandatory = new SimpleBooleanProperty();
    private ObjectProperty<ERModelEntity> entity = new SimpleObjectProperty<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ERModelEntity getEntity() {
        return entity.get();
    }

    public ObjectProperty<ERModelEntity> entityProperty() {
        return entity;
    }

    public void setEntity(ERModelEntity entity) {
        this.entity.set(entity);
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public SimpleBooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public ERModelRelationSideType getType() {
        return type;
    }

    public void setType(ERModelRelationSideType type) {
        this.type = type;
    }
}