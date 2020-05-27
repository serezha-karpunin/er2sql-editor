package com.etu.infrastructure.workflow.strategy.rm.link;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.workflow.service.WorkflowType;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.RM_REMOVE_LINK;

@Component
public class RModelRemoveLinkWorkflowStrategy implements RModelLinkWorkflowStrategy {

    @Override
    public void execute(RModelLink link) {

    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_REMOVE_LINK;
    }
}
