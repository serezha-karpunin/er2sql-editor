package com.etu.infrastructure.state.dto.serializable.erm;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType;

public class SerializableERModelRelationSide {
    private String id;
    private String entityId;
    private boolean mandatory;
    private ERModelRelationSideType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public ERModelRelationSideType getType() {
        return type;
    }

    public void setType(ERModelRelationSideType type) {
        this.type = type;
    }
}