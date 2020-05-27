package com.etu.infrastructure.event.dto;

import javafx.event.Event;
import javafx.event.EventType;

public class EventTypes {
    private static final EventType<Event> ANY = new EventType<>(Event.ANY, "APPLICATION_EVENT");

    public static final EventType<Event> PROJECT_OPENED = new EventType<>(ANY, "PROJECT_OPENED");
    public static final EventType<Event> PROJECT_CLOSED = new EventType<>(ANY, "PROJECT_CLOSED");

    public static final EventType<Event> RM_GENERATED = new EventType<>(ANY, "RM_GENERATED");
    public static final EventType<Event> SQL_GENERATED = new EventType<>(ANY, "SQL_GENERATED");

    public static final EventType<ERModelEntityEvent> ERM_ENTITY_CREATED = new EventType<>(ANY, "ERM_ENTITY_CREATED");
    public static final EventType<ERModelEntityEvent> ERM_ENTITY_SELECTED_TO_CREATE_RELATION = new EventType<>(ANY, "ERM_ENTITY_SELECTED_TO_CREATE_RELATION");
    public static final EventType<ERModelEntityEvent> ERM_ENTITY_DESELECTED_TO_CREATE_RELATION = new EventType<>(ANY, "ERM_ENTITY_DESELECTED_TO_CREATE_RELATION");
    public static final EventType<ERModelEntityEvent> ERM_ENTITY_REMOVED = new EventType<>(ANY, "ERM_ENTITY_REMOVED");

    public static final EventType<Event> ERM_RELATION_CREATION_STARTED = new EventType<>(ANY, "ERM_RELATION_CREATION_STARTED");
    public static final EventType<Event> ERM_RELATION_CREATION_CANCELLED = new EventType<>(ANY, "ERM_RELATION_CREATION_CANCELLED");
    public static final EventType<Event> ERM_RELATION_CREATION_CONFIRMED = new EventType<>(ANY, "ERM_RELATION_CREATION_CONFIRMED");
    public static final EventType<Event> ERM_RELATION_CREATION_FINISHED = new EventType<>(ANY, "ERM_RELATION_CREATION_FINISHED");

    public static final EventType<ERModelRelationEvent> ERM_RELATION_CREATED = new EventType<>(ANY, "ERM_RELATION_CREATED");
    public static final EventType<ERModelRelationEvent> ERM_RELATION_REMOVED = new EventType<>(ANY, "ERM_RELATION_REMOVED");

    public static final EventType<RModelRelationEvent> RM_RELATION_CREATED = new EventType<>(ANY, "RM_RELATION_CREATED");
    public static final EventType<RModelRelationEvent> RM_RELATION_SELECTED_TO_CREATE_LINK = new EventType<>(ANY, "RM_RELATION_SELECTED_TO_CREATE_LINK");
    public static final EventType<RModelRelationEvent> RM_RELATION_DESELECTED_TO_CREATE_LINK = new EventType<>(ANY, "RM_RELATION_DESELECTED_TO_CREATE_LINK");
    public static final EventType<RModelRelationEvent> RM_RELATION_REMOVED = new EventType<>(ANY, "RM_RELATION_REMOVED");

    public static final EventType<Event> RM_LINK_CREATION_STARTED = new EventType<>(ANY, "RM_LINK_CREATION_STARTED");
    public static final EventType<Event> RM_LINK_CREATION_CANCELLED = new EventType<>(ANY, "RM_LINK_CREATION_CANCELLED");
    public static final EventType<Event> RM_LINK_CREATION_CONFIRMED = new EventType<>(ANY, "RM_LINK_CREATION_CONFIRMED");
    public static final EventType<Event> RM_LINK_CREATION_FINISHED = new EventType<>(ANY, "RM_LINK_CREATION_FINISHED");

    public static final EventType<RModelLinkEvent> RM_LINK_CREATED = new EventType<>(ANY, "RM_LINK_CREATED");
    public static final EventType<RModelLinkEvent> RM_LINK_REMOVED = new EventType<>(ANY, "RM_LINK_REMOVED");
}
