package com.etu.infrastructure.event.dto;


import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import javafx.event.Event;
import javafx.event.EventType;

public class ERModelRelationEvent extends Event {
    private ERModelRelation relation;

    public ERModelRelationEvent(EventType<ERModelRelationEvent> eventType, ERModelRelation relation) {
        super(eventType);
        this.relation = relation;
    }

    public ERModelRelation getRelation() {
        return relation;
    }
}