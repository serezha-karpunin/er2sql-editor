package com.etu.infrastructure.state.dto.serializable.rm;

import java.util.List;

public class SerializableRModelState {
    private List<SerializableRModelRelation> relations;
    private List<SerializableRModelLink> links;

    public List<SerializableRModelRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<SerializableRModelRelation> relations) {
        this.relations = relations;
    }

    public List<SerializableRModelLink> getLinks() {
        return links;
    }

    public void setLinks(List<SerializableRModelLink> links) {
        this.links = links;
    }
}
