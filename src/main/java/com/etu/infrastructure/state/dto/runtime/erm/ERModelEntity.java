package com.etu.infrastructure.state.dto.runtime.erm;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelEntitySelectType.NOT_SELECTED;

public class ERModelEntity {
    private String id;
    private StringProperty name;
    private ListProperty<ERModelEntityAttribute> attributes;

    private ObjectProperty<ERModelEntitySelectType> selectType;
    private DoubleProperty layoutX;
    private DoubleProperty layoutY;
    private DoubleProperty width;
    private DoubleProperty height;

    public ERModelEntity() {
        this.name = new SimpleStringProperty();
        this.attributes = new SimpleListProperty<>(FXCollections.observableArrayList());

        this.selectType = new SimpleObjectProperty<>(NOT_SELECTED);
        this.layoutX = new SimpleDoubleProperty();
        this.layoutY = new SimpleDoubleProperty();
        this.width = new SimpleDoubleProperty();
        this.height = new SimpleDoubleProperty();
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

    public ObservableList<ERModelEntityAttribute> getAttributes() {
        return attributes.get();
    }

    public ListProperty<ERModelEntityAttribute> attributesProperty() {
        return attributes;
    }

    public void setAttributes(ObservableList<ERModelEntityAttribute> attributes) {
        this.attributes.set(attributes);
    }


    public ObjectProperty<ERModelEntitySelectType> selectTypeProperty() {
        return selectType;
    }

    public void setSelectType(ERModelEntitySelectType selectType) {
        this.selectType.set(selectType);
    }

    public ERModelEntitySelectType getSelectType() {
        return selectType.get();
    }

    public double getLayoutX() {
        return layoutX.get();
    }

    public double getCenterX() {
        return layoutX.get() + width.get() / 2;
    }

    public double getCenterY() {
        return layoutY.get() + height.get() / 2;
    }

    public DoubleProperty layoutXProperty() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX.set(layoutX);
    }

    public double getLayoutY() {
        return layoutY.get();
    }

    public DoubleProperty layoutYProperty() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY.set(layoutY);
    }

    public double getWidth() {
        return width.get();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }
}
