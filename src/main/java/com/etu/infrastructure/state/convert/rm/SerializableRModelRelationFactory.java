package com.etu.infrastructure.state.convert.rm;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelRelation;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelRelationAttribute;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SerializableRModelRelationFactory {

    public List<SerializableRModelRelation> create(List<RModelRelation> sources) {
        return sources.stream()
                .map(this::create)
                .collect(toList());
    }

    private SerializableRModelRelation create(RModelRelation source) {
        SerializableRModelRelation target = new SerializableRModelRelation();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setAttributes(createAttributes(source.getAttributes()));

        target.setX(source.getX());
        target.setY(source.getY());

        return target;
    }

    private List<SerializableRModelRelationAttribute> createAttributes(List<RModelRelationAttribute> sources) {
        return sources.stream()
                .map(this::createAttribute)
                .collect(toList());
    }

    private SerializableRModelRelationAttribute createAttribute(RModelRelationAttribute source) {
        SerializableRModelRelationAttribute target = new SerializableRModelRelationAttribute();
        target.setId(source.getId());
        target.setKey(source.isKey());
        target.setName(source.getName());

        target.setRelationId(source.getRelation().getId());
        if (source.getSourceAttribute() != null) {
            target.setSourceAttributeId(source.getSourceAttribute().getId());
        }

        target.setType(source.getType());

        return target;
    }
}
