package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationRelation;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.MANY;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.ONE;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.ONE_TO_MANY;
import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.*;

@Component
public class TransformOneToManyMandatoryRelationStep extends AbstractTransformRelationStep {

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, ONE_TO_MANY)
                && anySideMatch(relation, side -> hasType(side, ONE) && hasMandatory(side, true))
                && anySideMatch(relation, side -> hasType(side, MANY) && hasMandatory(side, true));
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {
        ERModelRelationSide manyRelationSide = getRelationSide(relation, MANY);
        TransformationEntity manyRelationSideEntity = getTransformationEntityFor(manyRelationSide.getEntity(), target);

        ERModelRelationSide oneRelationSide = getRelationSide(relation, ONE);
        TransformationEntity oneRelationSideEntity = getTransformationEntityFor(oneRelationSide.getEntity(), target);
        List<TransformationEntityAttribute> oneRelationSideEntityKeyAttributes = getKeyAttributes(oneRelationSideEntity);
        List<TransformationEntityAttribute> copiedKeyAttributes = copyAttributes(oneRelationSideEntityKeyAttributes);

        attachAsNonKeyAttributes(manyRelationSideEntity, copiedKeyAttributes);

        Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap = new LinkedHashMap<>();
        copiedKeyAttributes.forEach(attribute -> attributeMap.put(attribute.getSourceAttribute(), attribute));

        TransformationRelation oneToManyRelation = createOneToManyRelation(
                oneRelationSideEntity,
                manyRelationSideEntity,
                attributeMap
        );

        target.getRelations().add(oneToManyRelation);

    }
}
