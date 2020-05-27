package com.etu.infrastructure.workflow.strategy.rm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.RModelRelationEvent;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.rm.relation.RModelRelationWorkflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.RM_RELATION_DESELECTED_TO_CREATE_LINK;
import static com.etu.infrastructure.event.dto.EventTypes.RM_RELATION_SELECTED_TO_CREATE_LINK;
import static com.etu.infrastructure.state.dto.runtime.rm.RModelRelationSelectType.NOT_SELECTED;
import static com.etu.infrastructure.state.dto.runtime.rm.RModelRelationSelectType.SELECTED_TO_CREATE_LINK;
import static com.etu.infrastructure.workflow.service.WorkflowType.RM_SELECT_RELATION_TO_CREATE_LINK;

@Component
public class RModelSelectRelationToCreateLinkWorkflowStrategy implements RModelRelationWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute(RModelRelation relation) {
        switch (relation.getSelectType()) {
            case SELECTED_TO_CREATE_LINK:
                relation.setSelectType(NOT_SELECTED);
                eventFacade.fireEvent(new RModelRelationEvent(RM_RELATION_DESELECTED_TO_CREATE_LINK, relation));
                return;
            case NOT_SELECTED:
                relation.setSelectType(SELECTED_TO_CREATE_LINK);
                eventFacade.fireEvent(new RModelRelationEvent(RM_RELATION_SELECTED_TO_CREATE_LINK, relation));
                return;
            case SELECTED_TO_EDIT:
                throw new IllegalStateException();
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_SELECT_RELATION_TO_CREATE_LINK;
    }
}
