package com.etu.infrastructure.event.dto;


import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import javafx.event.Event;
import javafx.event.EventType;

public class RModelLinkEvent extends Event {
    private RModelLink link;

    public RModelLinkEvent(EventType<RModelLinkEvent> eventType, RModelLink link) {
        super(eventType);
        this.link = link;
    }

    public RModelLink getLink() {
        return link;
    }
}