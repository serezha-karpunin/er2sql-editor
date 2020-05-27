package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.*;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSideType;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkType;
import com.etu.infrastructure.workflow.strategy.transform.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class TransformationUtils {
    public static boolean hasType(ERModelRelation relation, ERModelRelationType type) {
        return relation.getRelationType() == type;
    }

    public static boolean allSidesMatch(ERModelRelation relation, Predicate<ERModelRelationSide> predicate) {
        return relation.getRelationSides().stream()
                .allMatch(predicate);
    }

    public static boolean anySideMatch(ERModelRelation relation, Predicate<ERModelRelationSide> predicate) {
        return relation.getRelationSides().stream()
                .anyMatch(predicate);
    }

    public static boolean hasType(ERModelRelationSide relationSide, ERModelRelationSideType type) {
        return relationSide.getType() == type;
    }

    public static boolean hasMandatory(ERModelRelationSide relationSide, boolean mandatory) {
        return relationSide.isMandatory() == mandatory;
    }

    public static List<TransformationEntity> getTransformationEntitiesFor(ERModelRelation relation, TransformationState target) {
        return relation.getRelationSides().stream()
                .map(ERModelRelationSide::getEntity)
                .map(entity -> getTransformationEntityFor(entity, target))
                .collect(toList());
    }

    public static TransformationEntity getTransformationEntityFor(ERModelEntity entity, TransformationState target) {
        return target.getEntities().stream()
                .filter(transformationEntity -> transformationEntity.getSourceEntities().contains(entity))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public static ERModelRelationSide getRelationSide(ERModelRelation relation, ERModelRelationSideType relationSideType) {
        return relation.getRelationSides().stream()
                .filter(side -> Objects.equals(side.getType(), relationSideType))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public static ERModelRelationSide getRelationSide(ERModelRelation relation, ERModelRelationSideType relationSideType, boolean mandatory) {
        return relation.getRelationSides().stream()
                .filter(side -> Objects.equals(side.getType(), relationSideType))
                .filter(side -> side.isMandatory() == mandatory)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public static List<TransformationEntityAttribute> getKeyAttributes(TransformationEntity entity) {
        return entity.getAttributes().stream()
                .filter(TransformationEntityAttribute::isKey)
                .collect(toList());
    }

    public static List<TransformationEntityAttribute> copyAttributes(List<TransformationEntityAttribute> attributes) {
        return attributes.stream()
                .map(TransformationUtils::copyAttribute)
                .collect(toList());
    }

    private static TransformationEntityAttribute copyAttribute(TransformationEntityAttribute source) {
        TransformationEntityAttribute copiedAttribute = new TransformationEntityAttribute();
        copiedAttribute.setId(UUID.randomUUID().toString());
        copiedAttribute.setKey(source.isKey());
        copiedAttribute.setName(source.getName());
        copiedAttribute.setSourceAttribute(source);

        return copiedAttribute;
    }

    public static void attachAsNonKeyAttributes(TransformationEntity entity, List<TransformationEntityAttribute> attributes) {
        attributes.forEach(attribute -> attribute.setKey(false));
        entity.getAttributes().addAll(attributes);
    }

    public static void attachAsKeyAttributes(TransformationEntity entity, List<TransformationEntityAttribute> attributes) {
        entity.getAttributes().addAll(0, attributes);
    }

    public static TransformationRelation createOneToOneRelation(TransformationEntity firstEntity,
                                                                TransformationEntity secondEntity,
                                                                Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap
    ) {
        TransformationRelation relation = new TransformationRelation();
        relation.setId(UUID.randomUUID().toString());
        relation.setRelationType(RModelLinkType.ONE_TO_ONE);

        TransformationRelationSide firstSide = new TransformationRelationSide();
        firstSide.setId(UUID.randomUUID().toString());
        firstSide.setType(RModelLinkSideType.ONE);
        firstSide.setEntity(firstEntity);
        relation.setRelationSideFrom(firstSide);

        TransformationRelationSide secondSide = new TransformationRelationSide();
        secondSide.setId(UUID.randomUUID().toString());
        secondSide.setType(RModelLinkSideType.ONE);
        secondSide.setEntity(secondEntity);
        relation.setRelationSideTo(secondSide);

        relation.setAttributeMap(attributeMap);

        return relation;
    }

    public static TransformationRelation createOneToManyRelation(TransformationEntity oneSideEntity,
                                                                 TransformationEntity manySideEntity,
                                                                 Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap
    ) {
        TransformationRelation relation = new TransformationRelation();
        relation.setId(UUID.randomUUID().toString());
        relation.setRelationType(RModelLinkType.ONE_TO_MANY);

        TransformationRelationSide oneRelationSide = new TransformationRelationSide();
        oneRelationSide.setId(UUID.randomUUID().toString());
        oneRelationSide.setType(RModelLinkSideType.ONE);
        oneRelationSide.setEntity(oneSideEntity);
        relation.setRelationSideFrom(oneRelationSide);


        TransformationRelationSide manyRelationSide = new TransformationRelationSide();
        manyRelationSide.setId(UUID.randomUUID().toString());
        manyRelationSide.setType(RModelLinkSideType.MANY);
        manyRelationSide.setEntity(manySideEntity);
        relation.setRelationSideTo(manyRelationSide);

        relation.setAttributeMap(attributeMap);

        return relation;
    }

    public static String getNameForNewEntity(ERModelRelation relation, List<TransformationEntity> entities) {
        String relationName = relation.getName();
        if (relationName != null) {
            return relationName;
        }

        return mergeNames(entities);
    }

    private static String mergeNames(List<TransformationEntity> entities) {
        return entities.stream()
                .map(TransformationEntity::getName)
                .collect(joining("-"));
    }

}
