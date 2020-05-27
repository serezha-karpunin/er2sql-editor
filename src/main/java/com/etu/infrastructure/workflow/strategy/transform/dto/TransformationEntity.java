package com.etu.infrastructure.workflow.strategy.transform.dto;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;

import java.util.ArrayList;
import java.util.List;

public class TransformationEntity {
    private String id;
    private String name;
    private List<ERModelEntity> sourceEntities = new ArrayList<>();
    private List<TransformationEntityAttribute> attributes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ERModelEntity> getSourceEntities() {
        return sourceEntities;
    }

    public void setSourceEntities(List<ERModelEntity> sourceEntities) {
        this.sourceEntities = sourceEntities;
    }

    public List<TransformationEntityAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<TransformationEntityAttribute> attributes) {
        this.attributes = attributes;
    }
}
