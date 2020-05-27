package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.*;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.ONE;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.ONE_TO_ONE;

@Component
public class TransformOneToOneNoneMandatoryRelationStep extends AbstractTransformRelationStep {

    @Autowired
    private TransformManyToManyRelationStep transformManyToManyRelationStep;

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, ONE_TO_ONE)
                && allSidesMatch(relation, side -> hasType(side, ONE) && hasMandatory(side, false));
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {
        transformManyToManyRelationStep.process(source, target, relation);
    }
}
