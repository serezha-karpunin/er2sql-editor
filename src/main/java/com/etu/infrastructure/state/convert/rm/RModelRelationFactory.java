package com.etu.infrastructure.state.convert.rm;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelRelation;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelRelationAttribute;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableList;

@Component
public class RModelRelationFactory {

    public List<RModelRelation> create(List<SerializableRModelRelation> sources, RModelState state) {
        List<RModelRelation> targets = sources.stream()
                .map(this::create)
                .collect(toList());

        resolveSourceAttributes(sources, targets, state);

        return targets;
    }

    private void resolveSourceAttributes(List<SerializableRModelRelation> sources, List<RModelRelation> targets, RModelState state) {
        for (SerializableRModelRelation source : sources) {
            for (SerializableRModelRelationAttribute serializableAttribute : source.getAttributes()) {
                if (serializableAttribute.getSourceAttributeId() != null) {
                    RModelRelationAttribute targetAttribute = getAttributeFor(serializableAttribute.getId(), targets);
                    RModelRelationAttribute targetSourceAttribute = getAttributeFor(serializableAttribute.getSourceAttributeId(), targets);

                    targetAttribute.setSourceAttribute(targetSourceAttribute);
                }
            }
        }
    }

    private RModelRelationAttribute getAttributeFor(String attributeId, List<RModelRelation> tables) {
        return tables.stream()
                .map(RModelRelation::getAttributes)
                .flatMap(Collection::stream)
                .filter(attr -> Objects.equals(attributeId, attr.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private RModelRelation create(SerializableRModelRelation source) {
        RModelRelation target = new RModelRelation();
        target.setId(source.getId());
        target.setName(source.getName());

        target.setAttributes(observableList(createAttributes(source, target)));
        target.getAttributes().forEach(attr -> attr.setRelation(target));

        target.setX(source.getX());
        target.setY(source.getY());

        return target;
    }

    private List<RModelRelationAttribute> createAttributes(SerializableRModelRelation sourceRelation, RModelRelation targetRelation) {
        return sourceRelation.getAttributes().stream()
                .map(source -> createAttribute(source, targetRelation))
                .collect(toList());
    }

    private RModelRelationAttribute createAttribute(SerializableRModelRelationAttribute source, RModelRelation relation) {
        RModelRelationAttribute target = new RModelRelationAttribute();
        target.setId(source.getId());
        target.setKey(source.isKey());
        target.setName(source.getName());
        target.setType(source.getType());
        target.setRelation(relation);

        return target;
    }
}
