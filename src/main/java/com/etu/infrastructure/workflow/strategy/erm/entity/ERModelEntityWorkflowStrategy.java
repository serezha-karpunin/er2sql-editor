package com.etu.infrastructure.workflow.strategy.erm.entity;

import com.etu.infrastructure.workflow.strategy.WorkflowStrategy;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;

public interface ERModelEntityWorkflowStrategy extends WorkflowStrategy {
    void execute(ERModelEntity entityDto);
}
