package com.etu.infrastructure.workflow.strategy.transform.dto;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkType;

import java.util.Map;

public class TransformationRelation {
    private String id;
    private RModelLinkType relationType;
    private TransformationRelationSide relationSideFrom;
    private TransformationRelationSide relationSideTo;
    private Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RModelLinkType getRelationType() {
        return relationType;
    }

    public void setRelationType(RModelLinkType relationType) {
        this.relationType = relationType;
    }

    public TransformationRelationSide getRelationSideFrom() {
        return relationSideFrom;
    }

    public void setRelationSideFrom(TransformationRelationSide relationSideFrom) {
        this.relationSideFrom = relationSideFrom;
    }

    public TransformationRelationSide getRelationSideTo() {
        return relationSideTo;
    }

    public void setRelationSideTo(TransformationRelationSide relationSideTo) {
        this.relationSideTo = relationSideTo;
    }

    public Map<TransformationEntityAttribute, TransformationEntityAttribute> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap) {
        this.attributeMap = attributeMap;
    }
}
