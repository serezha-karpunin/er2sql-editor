package com.etu.infrastructure.workflow.strategy.sql.dto;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;

public class SqlGenerationTableColumn {
    private boolean isPrimary;
    private String name;
    private RModelRelationAttributeDataType dataType;
    private RModelRelationAttribute sourceAttribute;

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RModelRelationAttributeDataType getDataType() {
        return dataType;
    }

    public void setDataType(RModelRelationAttributeDataType dataType) {
        this.dataType = dataType;
    }

    public RModelRelationAttribute getSourceAttribute() {
        return sourceAttribute;
    }

    public void setSourceAttribute(RModelRelationAttribute sourceAttribute) {
        this.sourceAttribute = sourceAttribute;
    }
}
