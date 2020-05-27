package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationRelation;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.*;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.MANY_TO_MANY;

@Component
public class TransformManyToManyRelationStep extends AbstractTransformRelationStep {

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, MANY_TO_MANY);
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {
        List<TransformationEntity> transformationEntities = getTransformationEntitiesFor(relation, target);

        TransformationEntity linkEntity = new TransformationEntity();
        linkEntity.setId(UUID.randomUUID().toString());
        linkEntity.setName(getNameForNewEntity(relation, transformationEntities));

        transformationEntities.forEach(entity -> {
            List<TransformationEntityAttribute> keyAttributes = getKeyAttributes(entity);
            List<TransformationEntityAttribute> copiedAttributes = copyAttributes(keyAttributes);
            attachAsNonKeyAttributes(linkEntity, copiedAttributes);

            Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap = new LinkedHashMap<>();
            copiedAttributes.forEach(attribute -> attributeMap.put(attribute.getSourceAttribute(), attribute));

            TransformationRelation transformationRelation = createOneToManyRelation(
                    entity,
                    linkEntity,
                    attributeMap
            );

            target.getRelations().add(transformationRelation);
        });

        target.getEntities().add(linkEntity);
    }
}
