package com.etu.infrastructure.workflow.strategy.rm.canvas;

import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.model.relational.RModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.state.dto.runtime.rm.RModelRelationSelectType.NOT_SELECTED;
import static com.etu.infrastructure.workflow.service.WorkflowType.RM_DISCARD_CANVAS_SELECTION;

@Component
public class RModelDiscardCanvasSelectionWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private RModelService rModelFacade;

    @Override
    public void execute() {
        rModelFacade.getCurrentRelations().forEach(relation -> relation.setSelectType(NOT_SELECTED));
        rModelFacade.getCurrentLinks().forEach(link -> link.setSelected(false));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_DISCARD_CANVAS_SELECTION;
    }
}
