package com.etu.infrastructure.workflow.strategy.erm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.erm.entity.ERModelEntityWorkflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelEntitySelectType.SELECTED_TO_EDIT;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_SELECT_ENTITY_TO_EDIT;

@Component
public class ERModelSelectEntityToEditWorkflowStrategy implements ERModelEntityWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;
    @Autowired
    @Qualifier("ERModelDiscardCanvasSelectionWorkflowStrategy")
    private SimpleWorkflowStrategy erModelDiscardCanvasSelectionWorkflowStrategy;

    @Override
    public void execute(ERModelEntity entityDto) {
        erModelDiscardCanvasSelectionWorkflowStrategy.execute();
        entityDto.setSelectType(SELECTED_TO_EDIT);
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_SELECT_ENTITY_TO_EDIT;
    }
}
