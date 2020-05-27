package com.etu.infrastructure.workflow.strategy.transform.dto;

public class TransformationEntityAttribute {

    private String id;
    private boolean key;
    private String name;
    private TransformationEntityAttribute sourceAttribute;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransformationEntityAttribute getSourceAttribute() {
        return sourceAttribute;
    }

    public void setSourceAttribute(TransformationEntityAttribute sourceAttribute) {
        this.sourceAttribute = sourceAttribute;
    }
}
