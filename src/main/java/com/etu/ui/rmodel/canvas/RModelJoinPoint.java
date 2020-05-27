package com.etu.ui.rmodel.canvas;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RModelJoinPoint {
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private ObjectProperty<RModelJoinPointSide> side = new SimpleObjectProperty<>(RModelJoinPointSide.LEFT);
    private ObjectProperty<RModelLink> relation = new SimpleObjectProperty<>();
    private ObjectProperty<RModelRelation> table = new SimpleObjectProperty<>();
    private ObjectProperty<RModelRelationAttribute> attribute = new SimpleObjectProperty<>();

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

    public RModelJoinPointSide getSide() {
        return side.get();
    }

    public ObjectProperty<RModelJoinPointSide> sideProperty() {
        return side;
    }

    public void setSide(RModelJoinPointSide side) {
        this.side.set(side);
    }

    public RModelLink getRelation() {
        return relation.get();
    }

    public ObjectProperty<RModelLink> relationProperty() {
        return relation;
    }

    public void setRelation(RModelLink relation) {
        this.relation.set(relation);
    }

    public RModelRelation getTable() {
        return table.get();
    }

    public ObjectProperty<RModelRelation> tableProperty() {
        return table;
    }

    public void setTable(RModelRelation table) {
        this.table.set(table);
    }

    public RModelRelationAttribute getAttribute() {
        return attribute.get();
    }

    public ObjectProperty<RModelRelationAttribute> attributeProperty() {
        return attribute;
    }

    public void setAttribute(RModelRelationAttribute attribute) {
        this.attribute.set(attribute);
    }

    public enum RModelJoinPointSide {
        LEFT, RIGHT
    }
}
