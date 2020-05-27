package com.etu.infrastructure.workflow.strategy.erm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_CONFIRM_RELATION_CREATION;

@Component
public class ERModelConfirmRelationCreationWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        eventFacade.fireEvent(new Event(EventTypes.ERM_RELATION_CREATION_CONFIRMED));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_CONFIRM_RELATION_CREATION;
    }
}
