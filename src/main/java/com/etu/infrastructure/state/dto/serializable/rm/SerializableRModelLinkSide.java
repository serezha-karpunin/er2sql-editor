package com.etu.infrastructure.state.dto.serializable.rm;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSideType;

public class SerializableRModelLinkSide {
    private String id;
    private RModelLinkSideType type;
    private String relationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RModelLinkSideType getType() {
        return type;
    }

    public void setType(RModelLinkSideType type) {
        this.type = type;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
