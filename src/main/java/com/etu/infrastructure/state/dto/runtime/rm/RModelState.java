package com.etu.infrastructure.state.dto.runtime.rm;

import java.util.ArrayList;
import java.util.List;

public class RModelState {
    private List<RModelRelation> relations;
    private List<RModelLink> links;

    public RModelState() {
        relations = new ArrayList<>();
        links = new ArrayList<>();
    }

    public List<RModelRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<RModelRelation> relations) {
        this.relations = relations;
    }

    public List<RModelLink> getLinks() {
        return links;
    }

    public void setLinks(List<RModelLink> links) {
        this.links = links;
    }
}
