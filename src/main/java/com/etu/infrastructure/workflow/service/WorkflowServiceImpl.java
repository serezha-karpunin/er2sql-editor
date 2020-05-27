package com.etu.infrastructure.workflow.service;

import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.erm.entity.ERModelEntityWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.erm.relation.ERModelRelationWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.rm.link.RModelLinkWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.rm.relation.RModelRelationWorkflowStrategy;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowServiceImpl implements WorkflowService {
    @Autowired
    private List<SimpleWorkflowStrategy> simpleWorkflowStrategies;
    @Autowired
    private List<ERModelEntityWorkflowStrategy> erModelEntityWorkflowStrategies;
    @Autowired
    private List<ERModelRelationWorkflowStrategy> erModelRelationWorkflowStrategies;
    @Autowired
    private List<RModelRelationWorkflowStrategy> rModelRelationWorkflowStrategies;
    @Autowired
    private List<RModelLinkWorkflowStrategy> rModelLinkWorkflowStrategies;

    @Override
    public void startProjectWorkflow(WorkflowType workflowType) {
        simpleWorkflowStrategies.stream()
                .filter(strategy -> workflowType == strategy.getWorkflowType())
                .findFirst()
                .ifPresent(SimpleWorkflowStrategy::execute);
    }

    @Override
    public void startErModelEntityWorkflow(WorkflowType workflowType, ERModelEntity entity) {
        erModelEntityWorkflowStrategies.stream()
                .filter(strategy -> workflowType == strategy.getWorkflowType())
                .findFirst()
                .ifPresent(strategy -> strategy.execute(entity));
    }

    @Override
    public void startErModelRelationWorkflow(WorkflowType workflowType, ERModelRelation relation) {
        erModelRelationWorkflowStrategies.stream()
                .filter(strategy -> workflowType == strategy.getWorkflowType())
                .findFirst()
                .ifPresent(strategy -> strategy.execute(relation));
    }

    @Override
    public void startRModelRelationWorkflow(WorkflowType workflowType, RModelRelation relation) {
        rModelRelationWorkflowStrategies.stream()
                .filter(strategy -> workflowType == strategy.getWorkflowType())
                .findFirst()
                .ifPresent(strategy -> strategy.execute(relation));
    }

    @Override
    public void startRModelLinkWorkflow(WorkflowType workflowType, RModelLink link) {
        rModelLinkWorkflowStrategies.stream()
                .filter(strategy -> workflowType == strategy.getWorkflowType())
                .findFirst()
                .ifPresent(strategy -> strategy.execute(link));
    }
}
