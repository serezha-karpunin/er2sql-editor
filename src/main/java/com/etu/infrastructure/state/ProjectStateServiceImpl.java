package com.etu.infrastructure.state;

import com.etu.infrastructure.file.FileService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.state.dto.runtime.project.ProjectState;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ProjectStateServiceImpl implements ProjectStateService {
    @Autowired
    private FileService fileService;

    private ProjectState projectState;

    @Override
    public ProjectState addNewProject(File projectFile) {
        ProjectState newState = new ProjectState();
        newState.setProjectName(projectFile.getName());
        newState.setProjectFile(projectFile);
        newState.setErModelState(new ERModelState());
        newState.setRModelState(new RModelState());

        projectState = newState;
        fileService.saveProjectState(projectState);

        return projectState;
    }

    @Override
    public ProjectState loadProject(File projectFile) {
        projectState = fileService.loadProjectState(projectFile);
        return getProjectState();
    }

    @Override
    public ProjectState saveProject() {
        fileService.saveProjectState(projectState);

        return projectState;
    }

    @Override
    public ProjectState saveProjectAs(File projectFile) {
        return null;
    }

    @Override
    public void closeProject() {
        projectState = null;
    }

    @Override
    public ProjectState getProjectState() {
        return projectState;
    }

}
