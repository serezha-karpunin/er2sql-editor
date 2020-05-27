package com.etu.infrastructure.state.dto.serializable.rm;

import java.util.List;

public class SerializableRModelRelation {

    private String id;
    private String name;
    private List<SerializableRModelRelationAttribute> attributes;

    private double x;
    private double y;

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

    public List<SerializableRModelRelationAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SerializableRModelRelationAttribute> attributes) {
        this.attributes = attributes;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
