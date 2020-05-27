package com.etu.infrastructure.workflow.service;

public enum WorkflowType {
    ADD_NEW_PROJECT,
    LOAD_PROJECT,
    SAVE_PROJECT,
    SAVE_PROJECT_AS,
    CLOSE_PROJECT,

    ERM_TRANSFORM,

    ERM_DISCARD_CANVAS_SELECTION,

    ERM_SELECT_ENTITY_TO_EDIT,
    ERM_SELECT_ENTITY_TO_CREATE_RELATION,
    ERM_ADD_ENTITY,
    ERM_EDIT_ENTITY,
    ERM_REMOVE_ENTITY,

    ERM_SELECT_RELATION,
    ERM_ADD_ONE_TO_ONE_RELATION,
    ERM_ADD_ONE_TO_MANY_RELATION,
    ERM_ADD_MANY_TO_MANY_RELATION,
    ERM_ADD_DEPENDENCY_RELATION,
    ERM_ADD_CATEGORY_RELATION,
    ERM_EDIT_RELATION,
    ERM_REMOVE_RELATION,

    ERM_CONFIRM_RELATION_CREATION,
    ERM_CANCEL_RELATION_CREATION,

    RM_DISCARD_CANVAS_SELECTION,

    RM_SELECT_RELATION_TO_EDIT,
    RM_SELECT_RELATION_TO_CREATE_LINK,
    RM_ADD_RELATION,
    RM_EDIT_RELATION,
    RM_REMOVE_RELATION,

    RM_SELECT_LINK,
    RM_ADD_LINK,
    RM_EDIT_LINK,
    RM_REMOVE_LINK,

    RM_CONFIRM_LINK_CREATION,
    RM_CANCEL_LINK_CREATION,

    SQL_GENERATION,

    EXPORT_ERM,
    EXPORT_RM,
    EXPORT_GENERATED_SQL,
}
