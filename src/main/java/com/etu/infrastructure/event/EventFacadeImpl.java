package com.etu.infrastructure.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import org.springframework.stereotype.Component;

@Component
public class EventFacadeImpl implements EventFacade {
    private Group group = new Group();

    @Override
    public void fireEvent(Event event) {
        group.fireEvent(event);
    }

    @Override
    public <T extends Event> void addEventListener(EventType<T> type, EventHandler<? super T> handler) {
        group.addEventHandler(type, handler);
    }

    @Override
    public <T extends Event> void removeEventListener(EventType<T> type, EventHandler<? super T> handler) {
        group.removeEventHandler(type, handler);
    }
}