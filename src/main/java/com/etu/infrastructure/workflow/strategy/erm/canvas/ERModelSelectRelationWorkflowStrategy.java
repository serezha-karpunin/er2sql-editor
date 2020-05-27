package com.etu.infrastructure.workflow.strategy.erm.canvas;

import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.erm.relation.ERModelRelationWorkflowStrategy;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_SELECT_RELATION;

@Component
public class ERModelSelectRelationWorkflowStrategy implements ERModelRelationWorkflowStrategy {
    @Autowired
    @Qualifier("ERModelDiscardCanvasSelectionWorkflowStrategy")
    private SimpleWorkflowStrategy erModelDiscardCanvasSelectionWorkflowStrategy;

    @Override
    public void execute(ERModelRelation relationDto) {
        erModelDiscardCanvasSelectionWorkflowStrategy.execute();
        relationDto.setSelected(true);
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_SELECT_RELATION;
    }
}
