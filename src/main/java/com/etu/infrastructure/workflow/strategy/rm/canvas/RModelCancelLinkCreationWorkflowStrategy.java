package com.etu.infrastructure.workflow.strategy.rm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.RM_LINK_CREATION_CANCELLED;
import static com.etu.infrastructure.workflow.service.WorkflowType.RM_CANCEL_LINK_CREATION;

@Component
public class RModelCancelLinkCreationWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        eventFacade.fireEvent(new Event(RM_LINK_CREATION_CANCELLED));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_CANCEL_LINK_CREATION;
    }
}
