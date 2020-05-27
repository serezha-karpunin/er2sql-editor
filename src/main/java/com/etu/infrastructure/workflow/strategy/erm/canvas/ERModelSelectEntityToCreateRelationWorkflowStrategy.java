package com.etu.infrastructure.workflow.strategy.erm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.ERModelEntityEvent;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.erm.entity.ERModelEntityWorkflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelEntitySelectType.NOT_SELECTED;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelEntitySelectType.SELECTED_TO_CREATE_RELATION;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_SELECT_ENTITY_TO_CREATE_RELATION;

@Component
public class ERModelSelectEntityToCreateRelationWorkflowStrategy implements ERModelEntityWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute(ERModelEntity entity) {
        switch (entity.getSelectType()) {
            case SELECTED_TO_CREATE_RELATION:
                entity.setSelectType(NOT_SELECTED);
                eventFacade.fireEvent(new ERModelEntityEvent(EventTypes.ERM_ENTITY_DESELECTED_TO_CREATE_RELATION, entity));
                return;
            case NOT_SELECTED:
                entity.setSelectType(SELECTED_TO_CREATE_RELATION);
                eventFacade.fireEvent(new ERModelEntityEvent(EventTypes.ERM_ENTITY_SELECTED_TO_CREATE_RELATION, entity));
                return;
            case SELECTED_TO_EDIT:
                throw new IllegalStateException();
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_SELECT_ENTITY_TO_CREATE_RELATION;
    }
}
