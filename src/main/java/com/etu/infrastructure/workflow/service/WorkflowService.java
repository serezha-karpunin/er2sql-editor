package com.etu.infrastructure.workflow.service;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;

public interface WorkflowService {
    void startProjectWorkflow(WorkflowType workflowType);

    void startErModelEntityWorkflow(WorkflowType workflowType, ERModelEntity entityDto);

    void startErModelRelationWorkflow(WorkflowType workflowType, ERModelRelation relationDto);

    void startRModelRelationWorkflow(WorkflowType workflowType, RModelRelation tableDto);

    void startRModelLinkWorkflow(WorkflowType workflowType, RModelLink relationDto);
}
