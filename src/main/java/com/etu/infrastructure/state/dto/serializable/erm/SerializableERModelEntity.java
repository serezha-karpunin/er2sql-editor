package com.etu.infrastructure.state.dto.serializable.erm;

import java.util.List;

public class SerializableERModelEntity {
    private String id;
    private String name;
    private List<SerializableERModelEntityAttribute> attributes;
    private double x;
    private double y;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SerializableERModelEntityAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SerializableERModelEntityAttribute> attributes) {
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
