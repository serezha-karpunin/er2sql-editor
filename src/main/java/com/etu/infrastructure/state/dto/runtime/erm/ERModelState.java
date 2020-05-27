package com.etu.infrastructure.state.dto.runtime.erm;

import java.util.ArrayList;
import java.util.List;

public class ERModelState {
    private List<ERModelEntity> entities;
    private List<ERModelRelation> relations;

    public ERModelState() {
        entities = new ArrayList<>();
        relations = new ArrayList<>();
    }

    public void setEntities(List<ERModelEntity> entities) {
        this.entities = entities;
    }

    public void setRelations(List<ERModelRelation> relations) {
        this.relations = relations;
    }

    public List<ERModelEntity> getEntities() {
        return entities;
    }

    public List<ERModelRelation> getRelations() {
        return relations;
    }
}
