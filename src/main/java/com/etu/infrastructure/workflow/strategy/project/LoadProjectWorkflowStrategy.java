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
import static com.etu.infrastructure.workflow.service.WorkflowType.LOAD_PROJECT;

@Component
public class LoadProjectWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private PrimaryStageProvider primaryStageProvider;
    @Autowired
    private FileChooser loadProjectFileChooser;
    @Autowired
    private ProjectStateService projectStateService;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute() {
        // TODO: 04.05.2020 Handle case when we already have opened project

        File file = loadProjectFileChooser.showOpenDialog(primaryStageProvider.getPrimaryStage());

        if (file != null) {
            projectStateService.loadProject(file);
            eventFacade.fireEvent(new Event(PROJECT_OPENED));
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return LOAD_PROJECT;
    }
}
