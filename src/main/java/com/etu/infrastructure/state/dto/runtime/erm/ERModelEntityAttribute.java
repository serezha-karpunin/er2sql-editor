package com.etu.infrastructure.state.dto.runtime.erm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ERModelEntityAttribute {
    private String id;
    private BooleanProperty key;
    private StringProperty name;

    public ERModelEntityAttribute() {
        this.key = new SimpleBooleanProperty();
        this.name = new SimpleStringProperty();
    }

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
}
