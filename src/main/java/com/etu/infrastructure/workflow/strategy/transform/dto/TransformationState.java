package com.etu.infrastructure.workflow.strategy.transform.dto;

import java.util.ArrayList;
import java.util.List;

public class TransformationState {
    private List<TransformationEntity> entities = new ArrayList<>();
    private List<TransformationRelation> relations = new ArrayList<>();

    public List<TransformationEntity> getEntities() {
        return entities;
    }

    public List<TransformationRelation> getRelations() {
        return relations;
    }
}
