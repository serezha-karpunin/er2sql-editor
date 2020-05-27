package com.etu.infrastructure.state.dto.serializable.rm;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkType;

import java.util.Map;

public class SerializableRModelLink {
    private String id;
    private RModelLinkType type;
    private SerializableRModelLinkSide linkSideFrom;
    private SerializableRModelLinkSide linkSideTo;
    private Map<String, String> linkedAttributesMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RModelLinkType getType() {
        return type;
    }

    public void setType(RModelLinkType type) {
        this.type = type;
    }

    public SerializableRModelLinkSide getLinkSideFrom() {
        return linkSideFrom;
    }

    public void setLinkSideFrom(SerializableRModelLinkSide linkSideFrom) {
        this.linkSideFrom = linkSideFrom;
    }

    public SerializableRModelLinkSide getLinkSideTo() {
        return linkSideTo;
    }

    public void setLinkSideTo(SerializableRModelLinkSide linkSideTo) {
        this.linkSideTo = linkSideTo;
    }

    public Map<String, String> getLinkedAttributesMap() {
        return linkedAttributesMap;
    }

    public void setLinkedAttributesMap(Map<String, String> linkedAttributesMap) {
        this.linkedAttributesMap = linkedAttributesMap;
    }
}
