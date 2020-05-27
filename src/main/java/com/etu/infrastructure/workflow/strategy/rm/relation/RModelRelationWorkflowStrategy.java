package com.etu.infrastructure.workflow.strategy.rm.relation;

import com.etu.infrastructure.workflow.strategy.WorkflowStrategy;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;

public interface RModelRelationWorkflowStrategy extends WorkflowStrategy {
    void execute(RModelRelation relation);
}
