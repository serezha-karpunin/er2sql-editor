package com.etu.infrastructure.state;

import com.etu.infrastructure.state.dto.runtime.project.ProjectState;

import java.io.File;

public interface ProjectStateService {
    ProjectState addNewProject(File projectFile);

    ProjectState loadProject(File projectFile);

    ProjectState saveProject();

    ProjectState saveProjectAs(File projectFile);

    ProjectState getProjectState();

    void closeProject();
}
