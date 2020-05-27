package com.etu.infrastructure.workflow.strategy.project;

import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.SAVE_PROJECT;

@Component
public class SaveProjectWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private ProjectStateService projectStateService;

    @Override
    public void execute() {
        projectStateService.saveProject();
    }

    @Override
    public WorkflowType getWorkflowType() {
        return SAVE_PROJECT;
    }
}
