package com.etu.infrastructure.workflow.strategy.project;

import com.etu.infrastructure.PrimaryStageProvider;
import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.etu.infrastructure.workflow.service.WorkflowType.SAVE_PROJECT_AS;

@Component
public class SaveProjectAsWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private PrimaryStageProvider primaryStageProvider;
    @Autowired
    private FileChooser saveProjectAsFileChooser;
    @Autowired
    private ProjectStateService projectStateService;

    @Override
    public void execute() {
        File file = saveProjectAsFileChooser.showSaveDialog(primaryStageProvider.getPrimaryStage());

        if (file != null) {
            projectStateService.saveProjectAs(file);
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return SAVE_PROJECT_AS;
    }
}
