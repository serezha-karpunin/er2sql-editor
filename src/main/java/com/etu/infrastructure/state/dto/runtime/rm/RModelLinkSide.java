package com.etu.infrastructure.state.dto.runtime.rm;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RModelLinkSide {
    private String id;
    private RModelLinkSideType type;
    private ObjectProperty<RModelRelation> relation = new SimpleObjectProperty<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RModelLinkSideType getType() {
        return type;
    }

    public void setType(RModelLinkSideType type) {
        this.type = type;
    }

    public RModelRelation getRelation() {
        return relation.get();
    }

    public ObjectProperty<RModelRelation> relationProperty() {
        return relation;
    }

    public void setRelation(RModelRelation relation) {
        this.relation.set(relation);
    }
}
