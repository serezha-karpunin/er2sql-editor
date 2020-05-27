package com.etu.infrastructure.workflow.strategy.project;

import com.etu.infrastructure.PrimaryStageProvider;
import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.event.Event;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.etu.infrastructure.event.dto.EventTypes.PROJECT_OPENED;
import static com.etu.infrastructure.workflow.service.WorkflowType.ADD_NEW_PROJECT;

@Component
public class AddNewProjectWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private PrimaryStageProvider primaryStageProvider;
    @Autowired
    private FileChooser addNewProjectFileChooser;
    @Autowired
    private ProjectStateService projectStateService;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        File file = addNewProjectFileChooser.showSaveDialog(primaryStageProvider.getPrimaryStage());

        if (file != null) {
            projectStateService.addNewProject(file);
            eventFacade.fireEvent(new Event(PROJECT_OPENED));
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ADD_NEW_PROJECT;
    }
}
