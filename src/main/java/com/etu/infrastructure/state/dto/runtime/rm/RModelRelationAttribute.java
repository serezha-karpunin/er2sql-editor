package com.etu.infrastructure.state.dto.runtime.rm;

import javafx.beans.property.*;

public class RModelRelationAttribute {

    private String id;
    private BooleanProperty key = new SimpleBooleanProperty();
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<RModelRelationAttributeDataType> type = new SimpleObjectProperty<>();

    private RModelRelation relation;
    private RModelRelationAttribute sourceAttribute;

    private double y;
    private double height;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isKey() {
        return key.get();
    }

    public BooleanProperty keyProperty() {
        return key;
    }

    public void setKey(boolean key) {
        this.key.set(key);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public RModelRelationAttributeDataType getType() {
        return type.get();
    }

    public ObjectProperty<RModelRelationAttributeDataType> typeProperty() {
        return type;
    }

    public void setType(RModelRelationAttributeDataType type) {
        this.type.set(type);
    }

    public RModelRelation getRelation() {
        return relation;
    }

    public void setRelation(RModelRelation relation) {
        this.relation = relation;
    }

    public RModelRelationAttribute getSourceAttribute() {
        return sourceAttribute;
    }

    public void setSourceAttribute(RModelRelationAttribute sourceAttribute) {
        this.sourceAttribute = sourceAttribute;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
