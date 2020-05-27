package com.etu.infrastructure.state.dto.serializable.erm;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType;

import java.util.List;

public class SerializableERModelRelation {
    private String id;
    private String name;
    private ERModelRelationType relationType;
    private List<SerializableERModelRelationSide> relationSides;

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

    public ERModelRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(ERModelRelationType relationType) {
        this.relationType = relationType;
    }

    public List<SerializableERModelRelationSide> getRelationSides() {
        return relationSides;
    }

    public void setRelationSides(List<SerializableERModelRelationSide> relationSides) {
        this.relationSides = relationSides;
    }


}
