package com.etu.infrastructure.state.dto.serializable.erm;

import java.util.List;

public class SerializableERModelState {
    private List<SerializableERModelEntity> entities;
    private List<SerializableERModelRelation> relations;

    public List<SerializableERModelEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<SerializableERModelEntity> entities) {
        this.entities = entities;
    }

    public List<SerializableERModelRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<SerializableERModelRelation> relations) {
        this.relations = relations;
    }
}
