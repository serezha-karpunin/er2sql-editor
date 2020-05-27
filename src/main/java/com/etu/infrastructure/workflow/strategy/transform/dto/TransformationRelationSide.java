package com.etu.infrastructure.workflow.strategy.transform.dto;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSideType;

public class TransformationRelationSide {
    private String id;
    private TransformationEntity entity;
    private RModelLinkSideType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransformationEntity getEntity() {
        return entity;
    }

    public void setEntity(TransformationEntity entity) {
        this.entity = entity;
    }

    public RModelLinkSideType getType() {
        return type;
    }

    public void setType(RModelLinkSideType type) {
        this.type = type;
    }
}
