package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationRelation;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.ONE;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.ONE_TO_ONE;
import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.*;

@Component
public class TransformOneToOneSomeMandatoryRelationStep extends AbstractTransformRelationStep {

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, ONE_TO_ONE)
                && anySideMatch(relation, side -> hasType(side, ONE) && hasMandatory(side, true))
                && anySideMatch(relation, side -> hasType(side, ONE) && hasMandatory(side, false));
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {
        ERModelRelationSide mandatoryRelationSide = getRelationSide(relation, ONE, true);
        TransformationEntity mandatoryEntity = getTransformationEntityFor(mandatoryRelationSide.getEntity(), target);

        ERModelRelationSide optionalRelationSide = getRelationSide(relation, ONE, false);
        TransformationEntity optionalEntity = getTransformationEntityFor(optionalRelationSide.getEntity(), target);
        List<TransformationEntityAttribute> optionalEntityKeyAttributes = getKeyAttributes(optionalEntity);
        List<TransformationEntityAttribute> copiedKeyAttributes = copyAttributes(optionalEntityKeyAttributes);

        attachAsNonKeyAttributes(mandatoryEntity, copiedKeyAttributes);

        Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap = new LinkedHashMap<>();
        copiedKeyAttributes.forEach(attribute -> attributeMap.put(attribute.getSourceAttribute(), attribute));

        TransformationRelation oneToOneRelation = createOneToOneRelation(
                optionalEntity,
                mandatoryEntity,
                attributeMap
        );

        target.getRelations().add(oneToOneRelation);
    }
}
