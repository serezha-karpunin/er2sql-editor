package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.workflow.strategy.transform.step.TransformStep;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;

import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class AbstractTransformRelationStep implements TransformStep {
    @Override
    public void process(ERModelState source, TransformationState target) {
        getRelationsToProcess(source, target).forEach(relation -> process(source, target, relation));
    }

    private List<ERModelRelation> getRelationsToProcess(ERModelState source, TransformationState target) {
        return source.getRelations().stream()
                .filter(this::canApply)
                .collect(toList());
    }

    protected abstract boolean canApply(ERModelRelation relation);

    protected abstract void process(ERModelState source, TransformationState target, ERModelRelation relation);
}
