package com.etu.infrastructure.workflow.strategy.erm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_CANCEL_RELATION_CREATION;

@Component
public class ERModelCancelRelationCreationWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        eventFacade.fireEvent(new Event(EventTypes.ERM_RELATION_CREATION_CANCELLED));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_CANCEL_RELATION_CREATION;
    }
}
