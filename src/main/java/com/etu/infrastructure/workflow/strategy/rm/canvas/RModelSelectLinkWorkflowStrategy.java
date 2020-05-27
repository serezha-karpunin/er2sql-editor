package com.etu.infrastructure.workflow.strategy.rm.canvas;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.rm.link.RModelLinkWorkflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.RM_SELECT_LINK;

@Component
public class RModelSelectLinkWorkflowStrategy implements RModelLinkWorkflowStrategy {
    @Autowired
    private RModelDiscardCanvasSelectionWorkflowStrategy rModelDiscardCanvasSelectionWorkflowStrategy;

    @Override
    public void execute(RModelLink link) {
        rModelDiscardCanvasSelectionWorkflowStrategy.execute();
        link.setSelected(true);
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_SELECT_LINK;
    }
}
