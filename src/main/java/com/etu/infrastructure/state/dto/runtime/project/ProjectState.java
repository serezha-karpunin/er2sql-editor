package com.etu.infrastructure.state.dto.runtime.project;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.state.dto.runtime.sql.SqlState;

import java.io.File;

public class ProjectState {
    private String projectName;
    private File projectFile;

    private ERModelState erModelState;
    private RModelState rModelState;
    private SqlState sqlState;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }

    public ERModelState getErModelState() {
        return erModelState;
    }

    public void setErModelState(ERModelState erModelState) {
        this.erModelState = erModelState;
    }

    public RModelState getRModelState() {
        return rModelState;
    }

    public void setRModelState(RModelState rModelState) {
        this.rModelState = rModelState;
    }

    public SqlState getSqlState() {
        return sqlState;
    }

    public void setSqlState(SqlState sqlState) {
        this.sqlState = sqlState;
    }
}
