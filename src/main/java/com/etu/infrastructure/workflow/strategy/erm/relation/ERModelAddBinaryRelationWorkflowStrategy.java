package com.etu.infrastructure.workflow.strategy.erm.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType;
import com.etu.infrastructure.workflow.service.WorkflowType;
import org.springframework.beans.factory.annotation.Autowired;

public class ERModelAddBinaryRelationWorkflowStrategy extends ERModelAddRelationWorkflowStrategy {

    private ERModelRelationSideType firstRelationSideType;
    private ERModelRelationSideType secondRelationSideType;

    @Autowired
    public ERModelAddBinaryRelationWorkflowStrategy(ERModelRelationType relationType, ERModelRelationSideType firstRelationSideType, ERModelRelationSideType secondRelationSideType, WorkflowType workflowType) {
        super(relationType, workflowType);
        this.firstRelationSideType = firstRelationSideType;
        this.secondRelationSideType = secondRelationSideType;
    }

    @Override
    protected boolean validateSelectedEntities() {
        return getSelectedEntities().size() == 2;
    }

    @Override
    protected ERModelRelation createRelation() {
        ERModelRelation relation = new ERModelRelation();

        ERModelRelationSide firstRelationSide = new ERModelRelationSide();
        firstRelationSide.setMandatory(true);
        firstRelationSide.setEntity(getSelectedEntities().get(0));
        firstRelationSide.setType(firstRelationSideType);

        ERModelRelationSide secondRelationSide = new ERModelRelationSide();
        secondRelationSide.setMandatory(true);
        secondRelationSide.setEntity(getSelectedEntities().get(1));
        secondRelationSide.setType(secondRelationSideType);

        relation.setRelationType(getRelationType());
        relation.getRelationSides().addAll(firstRelationSide, secondRelationSide);

        return relation;
    }
}
