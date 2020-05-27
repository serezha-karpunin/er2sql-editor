package com.etu.infrastructure.state.dto.runtime.rm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RModelLink {
    private String id;
    private RModelLinkType relationType;
    private ObjectProperty<RModelLinkSide> linkSideFrom = new SimpleObjectProperty<>();
    private ObjectProperty<RModelLinkSide> linkSideTo = new SimpleObjectProperty<>();
    private Map<RModelRelationAttribute, RModelRelationAttribute> linkedAttributesMap = new LinkedHashMap<>();

    private BooleanProperty selected = new SimpleBooleanProperty(false);

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

    public RModelLinkSide getLinkSideFrom() {
        return linkSideFrom.get();
    }

    public ObjectProperty<RModelLinkSide> linkSideFromProperty() {
        return linkSideFrom;
    }

    public void setLinkSideFrom(RModelLinkSide linkSideFrom) {
        this.linkSideFrom.set(linkSideFrom);
    }

    public RModelLinkSide getLinkSideTo() {
        return linkSideTo.get();
    }

    public ObjectProperty<RModelLinkSide> linkSideToProperty() {
        return linkSideTo;
    }

    public void setLinkSideTo(RModelLinkSide linkSideTo) {
        this.linkSideTo.set(linkSideTo);
    }

    public Map<RModelRelationAttribute, RModelRelationAttribute> getLinkedAttributesMap() {
        return linkedAttributesMap;
    }

    public void setLinkedAttributesMap(Map<RModelRelationAttribute, RModelRelationAttribute> linkedAttributesMap) {
        this.linkedAttributesMap = linkedAttributesMap;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }


}
