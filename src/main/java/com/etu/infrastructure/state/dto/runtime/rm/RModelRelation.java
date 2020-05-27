package com.etu.infrastructure.state.dto.runtime.rm;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;

import java.util.ArrayList;

public class RModelRelation {
    private String id;
    private StringProperty name;
    private ListProperty<RModelRelationAttribute> attributes;

    private ObjectProperty<RModelRelationSelectType> selectType;
    private ObjectProperty<Bounds> bounds;
    private double x;
    private double y;
    private double width;
    private double height;

    public RModelRelation() {
        this.name = new SimpleStringProperty();
        this.attributes = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        this.selectType = new SimpleObjectProperty<>();
        this.bounds = new SimpleObjectProperty<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ObservableList<RModelRelationAttribute> getAttributes() {
        return attributes.get();
    }

    public ListProperty<RModelRelationAttribute> attributesProperty() {
        return attributes;
    }

    public void setAttributes(ObservableList<RModelRelationAttribute> attributes) {
        this.attributes.set(attributes);
    }

    public RModelRelationSelectType getSelectType() {
        return selectType.get();
    }

    public ObjectProperty<RModelRelationSelectType> selectTypeProperty() {
        return selectType;
    }

    public void setSelectType(RModelRelationSelectType selectType) {
        this.selectType.set(selectType);
    }

    public Bounds getBounds() {
        return bounds.get();
    }

    public ObjectProperty<Bounds> boundsProperty() {
        return bounds;
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setBounds(Bounds bounds) {
        this.bounds.set(bounds);
    }

    public double getCenterX() {
        return x + width / 2;
    }
}
