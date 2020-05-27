package com.etu.infrastructure.workflow.strategy.project;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.PROJECT_CLOSED;
import static com.etu.infrastructure.workflow.service.WorkflowType.CLOSE_PROJECT;

@Component
public class CloseProjectWorkflowStrategy implements SimpleWorkflowStrategy {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SaveProjectWorkflowStrategy saveProjectWorkflowStrategy;
    @Autowired
    private ProjectStateService projectStateService;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        applicationContext.getBean("saveOnCloseAlert", Alert.class).showAndWait()
                .map(ButtonType::getButtonData)
                .ifPresent(this::handleAlertResult);
    }

    private void handleAlertResult(ButtonBar.ButtonData alertResult) {
        switch (alertResult) {
            case YES:
                saveProjectWorkflowStrategy.execute();
                closeProject();
                break;
            case NO:
                closeProject();
                break;
            case CANCEL_CLOSE:
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void closeProject() {
        projectStateService.closeProject();
        eventFacade.fireEvent(new Event(PROJECT_CLOSED));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return CLOSE_PROJECT;
    }
}
