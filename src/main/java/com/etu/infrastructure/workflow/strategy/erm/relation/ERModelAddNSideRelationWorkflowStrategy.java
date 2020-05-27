package com.etu.infrastructure.workflow.strategy.erm.relation;

import com.etu.infrastructure.state.dto.runtime.erm.*;
import com.etu.infrastructure.workflow.service.WorkflowType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_ADD_MANY_TO_MANY_RELATION;

public class ERModelAddNSideRelationWorkflowStrategy extends ERModelAddRelationWorkflowStrategy {

    private ERModelRelationSideType relationSideType;

    @Autowired
    public ERModelAddNSideRelationWorkflowStrategy(ERModelRelationType relationType, ERModelRelationSideType relationSideType, WorkflowType workflowType) {
        super(relationType, workflowType);
        this.relationSideType = relationSideType;
    }

    @Override
    protected boolean validateSelectedEntities() {
        return getSelectedEntities().size() >= 2;
    }

    @Override
    protected ERModelRelation createRelation() {
        ERModelRelation relation = new ERModelRelation();

        List<ERModelRelationSide> relationSides = getSelectedEntities().stream()
                .map(this::createRelationSide)
                .collect(Collectors.toList());

        relation.setRelationType(getRelationType());
        relation.getRelationSides().addAll(relationSides);

        return relation;
    }

    private ERModelRelationSide createRelationSide(ERModelEntity entity) {
        ERModelRelationSide relationSide = new ERModelRelationSide();

        relationSide.setMandatory(true);
        relationSide.setEntity(entity);
        relationSide.setType(relationSideType);

        return relationSide;
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_ADD_MANY_TO_MANY_RELATION;
    }
}
