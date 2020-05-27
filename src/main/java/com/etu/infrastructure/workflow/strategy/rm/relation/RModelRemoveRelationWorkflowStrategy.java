package com.etu.infrastructure.workflow.strategy.rm.relation;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowType;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.RM_REMOVE_RELATION;

@Component
public class RModelRemoveRelationWorkflowStrategy implements RModelRelationWorkflowStrategy {
    @Override
    public void execute(RModelRelation relation) {

    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_REMOVE_RELATION;
    }
}
