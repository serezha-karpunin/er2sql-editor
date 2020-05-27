package com.etu.infrastructure.state.dto.serializable.rm;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;

public class SerializableRModelRelationAttribute {
    private String id;
    private boolean key;
    private String name;

    private String relationId;
    private String sourceAttributeId;

    private RModelRelationAttributeDataType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getSourceAttributeId() {
        return sourceAttributeId;
    }

    public void setSourceAttributeId(String sourceAttributeId) {
        this.sourceAttributeId = sourceAttributeId;
    }

    public RModelRelationAttributeDataType getType() {
        return type;
    }

    public void setType(RModelRelationAttributeDataType type) {
        this.type = type;
    }
}
