package com.etu.infrastructure.workflow.strategy.rm.canvas;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.RM_LINK_CREATION_CONFIRMED;
import static com.etu.infrastructure.workflow.service.WorkflowType.RM_CONFIRM_LINK_CREATION;

@Component
public class RModelConfirmLinkCreationWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        eventFacade.fireEvent(new Event(RM_LINK_CREATION_CONFIRMED));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_CONFIRM_LINK_CREATION;
    }
}
