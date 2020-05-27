package com.etu.infrastructure.event.dto;


import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import javafx.event.Event;
import javafx.event.EventType;

public class ERModelEntityEvent extends Event {
    private ERModelEntity entity;

    public ERModelEntityEvent(EventType<ERModelEntityEvent> eventType, ERModelEntity entity) {
        super(eventType);
        this.entity = entity;
    }

    public ERModelEntity getEntity() {
        return entity;
    }
}