package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.hasType;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.CATEGORY;

@Component
public class TransformCategoryRelationStep extends AbstractTransformRelationStep {

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, CATEGORY);
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {
        throw new UnsupportedOperationException();
    }
}

//
//    ERModelRelationSide parentSide = getRelationSide(relation, PARENT);
//    ModelTransformationEntity parentEntity = tState.getEntityForSourceId(parentSide.getEntityId());
//    List<ModelTransformationEntityAttribute> parentKeyAttributes = parentEntity.getKeyAttributes();
//
//    ERModelRelationSide specificSide = getRelationSide(relation, SPECIFIC);
//    ModelTransformationEntity specificEntity = tState.getEntityForSourceId(specificSide.getEntityId());
//
//        if (parentEntityShouldExist(relation, ermState, tState)) {
//                List<ModelTransformationEntityAttribute> copiedAttributes = copyAttributes(parentKeyAttributes);
//        copiedAttributes.forEach(attribute -> {
//        attribute.setEntityId(specificEntity.getId());
//        attribute.setKey(false);
//        });
//
//        specificEntity.getAttributes().addAll(copiedAttributes);
//
//        ModelTransformationRelation oneToOneRelation = getModelTransformationRelationFactory().createOneToOneRelation(
//        parentEntity,
//        parentKeyAttributes,
//        specificEntity,
//        copiedAttributes
//        );
//
//        tState.getRelations().add(oneToOneRelation);
//        } else {
//        parentEntity.setAbstract(true);
//        //copy all attrs as non key (but if there is no key then mark as key)
//        }