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

import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.DEPENDENT;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.MAIN;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.DEPENDS_ON;
import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.*;

@Component
public class TransformDependencyRelationStep extends AbstractTransformRelationStep {

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, DEPENDS_ON);
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {

        ERModelRelationSide dependentRelationSide = getRelationSide(relation, DEPENDENT);
        TransformationEntity dependentTransformationEntity = getTransformationEntityFor(dependentRelationSide.getEntity(), target);

        ERModelRelationSide mainRelationSide = getRelationSide(relation, MAIN);
        TransformationEntity mainTransformationEntity = getTransformationEntityFor(mainRelationSide.getEntity(), target);
        List<TransformationEntityAttribute> mainTransformationEntityKeyAttributes = getKeyAttributes(mainTransformationEntity);
        List<TransformationEntityAttribute> copiedKeyAttributes = copyAttributes(mainTransformationEntityKeyAttributes);

        attachAsKeyAttributes(dependentTransformationEntity, copiedKeyAttributes);

        Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap = new LinkedHashMap<>();
        copiedKeyAttributes.forEach(attribute -> attributeMap.put(attribute.getSourceAttribute(), attribute));

        TransformationRelation oneToManyRelation = createOneToManyRelation(
                mainTransformationEntity,
                dependentTransformationEntity,
                attributeMap
        );

        target.getRelations().add(oneToManyRelation);
    }
}
