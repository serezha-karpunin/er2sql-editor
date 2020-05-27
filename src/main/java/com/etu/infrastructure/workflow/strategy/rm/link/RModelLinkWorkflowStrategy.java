package com.etu.infrastructure.workflow.strategy.rm.link;

import com.etu.infrastructure.workflow.strategy.WorkflowStrategy;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;

public interface RModelLinkWorkflowStrategy extends WorkflowStrategy {
    void execute(RModelLink relation);
}
