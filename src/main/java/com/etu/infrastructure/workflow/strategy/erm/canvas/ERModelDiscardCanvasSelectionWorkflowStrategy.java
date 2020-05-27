package com.etu.infrastructure.workflow.strategy.erm.canvas;

import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.model.er.ERModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelEntitySelectType.NOT_SELECTED;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_DISCARD_CANVAS_SELECTION;

@Component
public class ERModelDiscardCanvasSelectionWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private ERModelService erModelFacade;

    @Override
    public void execute() {
        erModelFacade.getCurrentEntities().forEach(entity -> entity.setSelectType(NOT_SELECTED));
        erModelFacade.getCurrentRelations().forEach(relation -> relation.setSelected(false));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_DISCARD_CANVAS_SELECTION;
    }
}
