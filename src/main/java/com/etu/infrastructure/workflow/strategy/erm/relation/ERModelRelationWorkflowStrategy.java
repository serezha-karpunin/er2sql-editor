package com.etu.infrastructure.workflow.strategy.erm.relation;

import com.etu.infrastructure.workflow.strategy.WorkflowStrategy;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;

public interface ERModelRelationWorkflowStrategy extends WorkflowStrategy {
    void execute(ERModelRelation relationDto);
}
