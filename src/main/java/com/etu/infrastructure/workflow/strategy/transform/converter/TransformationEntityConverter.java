package com.etu.infrastructure.workflow.strategy.transform.converter;

import com.etu.infrastructure.convert.Converter;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class TransformationEntityConverter implements Converter<TransformationEntity, RModelRelation> {
    @Override
    public RModelRelation convert(TransformationEntity transformationEntity) {
        RModelRelation table = new RModelRelation();
        table.setId(transformationEntity.getId());
        table.setName(transformationEntity.getName());
        table.getAttributes().addAll(convertAttributes(transformationEntity.getAttributes()));
        table.getAttributes().forEach(attr -> attr.setRelation(table));

        return table;
    }

    private List<RModelRelationAttribute> convertAttributes(List<TransformationEntityAttribute> transformationEntityAttributes) {
        List<RModelRelationAttribute> attributes = transformationEntityAttributes.stream()
                .map(this::createAttributeFor)
                .collect(toList());

        return attributes;
    }

    private RModelRelationAttribute createAttributeFor(TransformationEntityAttribute transformationEntityAttribute) {
        RModelRelationAttribute attribute = new RModelRelationAttribute();

        attribute.setId(transformationEntityAttribute.getId());
        attribute.setName(transformationEntityAttribute.getName());
        attribute.setKey(transformationEntityAttribute.isKey());
        attribute.setType(RModelRelationAttributeDataType.CHAR);

        return attribute;
    }
}
