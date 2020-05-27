package com.etu.infrastructure.workflow.strategy.rm.canvas;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.rm.relation.RModelRelationWorkflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.state.dto.runtime.rm.RModelRelationSelectType.SELECTED_TO_EDIT;
import static com.etu.infrastructure.workflow.service.WorkflowType.RM_SELECT_RELATION_TO_EDIT;

@Component
public class RModelSelectRelationToEditWorkflowStrategy implements RModelRelationWorkflowStrategy {
    @Autowired
    private RModelDiscardCanvasSelectionWorkflowStrategy rModelDiscardCanvasSelectionWorkflowStrategy;

    @Override
    public void execute(RModelRelation relation) {
        rModelDiscardCanvasSelectionWorkflowStrategy.execute();
        relation.setSelectType(SELECTED_TO_EDIT);
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_SELECT_RELATION_TO_EDIT;
    }
}
