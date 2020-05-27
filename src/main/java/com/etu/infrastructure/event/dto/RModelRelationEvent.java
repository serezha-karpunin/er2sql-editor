package com.etu.infrastructure.event.dto;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import javafx.event.Event;
import javafx.event.EventType;

public class RModelRelationEvent extends Event {
    private RModelRelation relation;

    public RModelRelationEvent(EventType<RModelRelationEvent> eventType, RModelRelation relation) {
        super(eventType);
        this.relation = relation;
    }

    public RModelRelation getRelation() {
        return relation;
    }
}
