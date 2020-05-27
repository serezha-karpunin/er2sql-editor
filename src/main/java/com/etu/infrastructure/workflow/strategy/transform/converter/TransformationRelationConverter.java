package com.etu.infrastructure.workflow.strategy.transform.converter;

import com.etu.infrastructure.convert.StateAwareConverter;
import com.etu.infrastructure.state.dto.runtime.rm.*;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationRelation;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationRelationSide;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TransformationRelationConverter implements StateAwareConverter<TransformationRelation, RModelLink, RModelState> {
    @Override
    public RModelLink convert(TransformationRelation transformationRelation, RModelState rModelState) {
        RModelLink relation = new RModelLink();
        relation.setId(transformationRelation.getId());
        relation.setRelationType(transformationRelation.getRelationType());
        relation.setLinkSideFrom(convertLinkSide(transformationRelation.getRelationSideFrom(), rModelState));
        relation.setLinkSideTo(convertLinkSide(transformationRelation.getRelationSideTo(), rModelState));
        relation.setLinkedAttributesMap(convertLinkedAttributesMap(transformationRelation.getAttributeMap(), rModelState));

        return relation;
    }

    private RModelLinkSide convertLinkSide(TransformationRelationSide transformationRelationSide, RModelState rModelState) {
        RModelLinkSide linkSide = new RModelLinkSide();

        linkSide.setId(transformationRelationSide.getId());
        linkSide.setType(transformationRelationSide.getType());
        linkSide.setRelation(findRelationFor(transformationRelationSide, rModelState));

        return linkSide;
    }

    private RModelRelation findRelationFor(TransformationRelationSide transformationRelationSide, RModelState rModelState) {
        return rModelState.getRelations().stream()
                .filter(table -> Objects.equals(table.getId(), transformationRelationSide.getEntity().getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Map<RModelRelationAttribute, RModelRelationAttribute> convertLinkedAttributesMap(Map<TransformationEntityAttribute, TransformationEntityAttribute> attributeMap, RModelState rModelState) {
        Map<RModelRelationAttribute, RModelRelationAttribute> linkedAttributesMap = new LinkedHashMap<>();

        attributeMap.forEach(
                (key, value) -> linkedAttributesMap.put(
                        findAttributeFor(key, rModelState),
                        findAttributeFor(value, rModelState)
                )
        );

        return linkedAttributesMap;
    }

    private RModelRelationAttribute findAttributeFor(TransformationEntityAttribute source, RModelState rModelState) {
        return rModelState.getRelations().stream()
                .map(RModelRelation::getAttributes)
                .flatMap(Collection::stream)
                .filter(attribute -> Objects.equals(attribute.getId(), source.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
