package com.etu.infrastructure.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface EventFacade {
    void fireEvent(Event event);

    <T extends Event> void addEventListener(EventType<T> type, EventHandler<? super T> handler);

    <T extends Event> void removeEventListener(EventType<T> type, EventHandler<? super T> handler);
}