package com.etu.infrastructure.file;

import com.etu.infrastructure.state.dto.runtime.project.ProjectState;

import java.io.File;

public interface FileService {
    ProjectState loadProjectState(File file);

    void saveProjectState(ProjectState projectState);

    void saveSql(File file, String text);
}
