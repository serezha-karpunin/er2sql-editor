package com.etu.infrastructure.workflow.strategy.transform.converter;

import com.etu.infrastructure.convert.Converter;
import com.etu.infrastructure.convert.StateAwareConverter;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationRelation;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class TransformationStateConverter implements Converter<TransformationState, RModelState> {

    @Autowired
    private Converter<TransformationEntity, RModelRelation> transformationEntityConverter;
    @Autowired
    private StateAwareConverter<TransformationRelation, RModelLink, RModelState> transformationRelationConverter;

    @Override
    public RModelState convert(TransformationState transformationState) {
        RModelState rmState = new RModelState();
        rmState.setRelations(convertEntities(transformationState.getEntities(), transformationState));
        rmState.setLinks(convertRelations(transformationState.getRelations(), rmState));

        return rmState;
    }

    private List<RModelRelation> convertEntities(List<TransformationEntity> transformationEntities, TransformationState transformationState) {
        List<RModelRelation> tables = transformationEntityConverter.convertAll(transformationEntities);
        // TODO: 17.05.2020 move logic to converter
        resolveSourceAttributes(tables, transformationState);

        return tables;
    }

    private void resolveSourceAttributes(List<RModelRelation> tables, TransformationState transformationState) {
        tables.forEach(table -> resolveSourceAttributes(table, tables, transformationState));
    }

    private void resolveSourceAttributes(RModelRelation table, List<RModelRelation> tables, TransformationState transformationState) {
        table.getAttributes().forEach(
                attribute -> {
                    TransformationEntityAttribute tSourceAttr = findTransformationAttributeFor(attribute, transformationState).getSourceAttribute();
                    if (tSourceAttr != null) {
                        attribute.setSourceAttribute(findTableAttributeFor(tSourceAttr, tables));
                    }
                }
        );
    }

    private RModelRelationAttribute findTableAttributeFor(TransformationEntityAttribute tSourceAttr, List<RModelRelation> tables) {
        return tables.stream()
                .map(RModelRelation::getAttributes)
                .flatMap(Collection::stream)
                .filter(attr -> Objects.equals(tSourceAttr.getId(), attr.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private TransformationEntityAttribute findTransformationAttributeFor(RModelRelationAttribute attribute, TransformationState transformationState) {
        return transformationState.getEntities().stream()
                .map(TransformationEntity::getAttributes)
                .flatMap(Collection::stream)
                .filter(tAttr -> Objects.equals(attribute.getId(), tAttr.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private List<RModelLink> convertRelations(List<TransformationRelation> relations, RModelState rmState) {
        return transformationRelationConverter.convertAll(relations, rmState);
    }
}
