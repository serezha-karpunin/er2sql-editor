package com.etu.ui.ermodel.canvas;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ERModelJoinPoint {
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private ObjectProperty<ERModelJoinPointSide> side = new SimpleObjectProperty<>();
    private ObjectProperty<ERModelRelation> relation = new SimpleObjectProperty<>();
    private ObjectProperty<ERModelEntity> entity = new SimpleObjectProperty<>();

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public ERModelJoinPointSide getSide() {
        return side.get();
    }

    public ObjectProperty<ERModelJoinPointSide> sideProperty() {
        return side;
    }

    public void setSide(ERModelJoinPointSide side) {
        this.side.set(side);
    }

    public ERModelRelation getRelation() {
        return relation.get();
    }

    public ObjectProperty<ERModelRelation> relationProperty() {
        return relation;
    }

    public void setRelation(ERModelRelation relation) {
        this.relation.set(relation);
    }

    public ERModelEntity getEntity() {
        return entity.get();
    }

    public ObjectProperty<ERModelEntity> entityProperty() {
        return entity;
    }

    public void setEntity(ERModelEntity entity) {
        this.entity.set(entity);
    }

    public enum ERModelJoinPointSide {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
