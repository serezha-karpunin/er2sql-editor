package com.etu.infrastructure.state.dto.serializable.project;

import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelState;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelState;

public class SerializableProjectState {
    private String projectName;
    private SerializableERModelState erModelState;
    private SerializableRModelState rModelState;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public SerializableERModelState getErModelState() {
        return erModelState;
    }

    public void setErModelState(SerializableERModelState erModelState) {
        this.erModelState = erModelState;
    }

    public SerializableRModelState getRModelState() {
        return rModelState;
    }

    public void setRModelState(SerializableRModelState rModelState) {
        this.rModelState = rModelState;
    }
}
