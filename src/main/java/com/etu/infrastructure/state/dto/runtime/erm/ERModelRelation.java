package com.etu.infrastructure.state.dto.runtime.erm;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ERModelRelation {
    private String id;
    private StringProperty name;
    private ObjectProperty<ERModelRelationType> relationType;
    private ListProperty<ERModelRelationSide> relationSides;

    private BooleanProperty selected;

    public ERModelRelation() {
        name = new SimpleStringProperty();
        relationType = new SimpleObjectProperty<>();
        relationSides = new SimpleListProperty<>(FXCollections.observableArrayList());
        selected = new SimpleBooleanProperty();
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

    public ERModelRelationType getRelationType() {
        return relationType.get();
    }

    public ObjectProperty<ERModelRelationType> relationTypeProperty() {
        return relationType;
    }

    public void setRelationType(ERModelRelationType relationType) {
        this.relationType.set(relationType);
    }

    public ObservableList<ERModelRelationSide> getRelationSides() {
        return relationSides.get();
    }

    public ListProperty<ERModelRelationSide> relationSidesProperty() {
        return relationSides;
    }

    public void setRelationSides(ObservableList<ERModelRelationSide> relationSides) {
        this.relationSides.set(relationSides);
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
