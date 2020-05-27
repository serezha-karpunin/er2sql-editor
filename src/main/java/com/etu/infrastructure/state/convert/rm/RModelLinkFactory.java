package com.etu.infrastructure.state.convert.rm;

import com.etu.infrastructure.state.dto.runtime.rm.*;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelLink;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelLinkSide;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component
public class RModelLinkFactory {

    public List<RModelLink> create(List<SerializableRModelLink> sources, RModelState state) {
        return sources.stream()
                .map(source -> create(source, state))
                .collect(toList());
    }

    private RModelLink create(SerializableRModelLink source, RModelState state) {
        RModelLink target = new RModelLink();
        target.setId(source.getId());
        target.setRelationType(source.getType());
        target.setLinkSideFrom(createLinkSide(source.getLinkSideFrom(), state));
        target.setLinkSideTo(createLinkSide(source.getLinkSideTo(), state));
        target.setLinkedAttributesMap(createLinkedAttributeMap(source.getLinkedAttributesMap(), state));

        return target;
    }

    private Map<RModelRelationAttribute, RModelRelationAttribute> createLinkedAttributeMap(Map<String, String> source, RModelState state) {
        Map<RModelRelationAttribute, RModelRelationAttribute> target = new LinkedHashMap<>();
        source.forEach((k, v) -> target.put(getAttributeFor(k, state), getAttributeFor(v, state)));

        return target;
    }

    private RModelRelationAttribute getAttributeFor(String attributeId, RModelState state) {
        return state.getRelations().stream()
                .map(RModelRelation::getAttributes)
                .flatMap(Collection::stream)
                .filter(attribute -> attribute.getId().equals(attributeId))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private RModelLinkSide createLinkSide(SerializableRModelLinkSide source, RModelState state) {
        RModelLinkSide target = new RModelLinkSide();
        target.setId(source.getId());
        target.setType(source.getType());
        target.setRelation(getRelationForId(source.getRelationId(), state));

        return target;
    }

    private RModelRelation getRelationForId(String relationId, RModelState state) {
        return state.getRelations().stream()
                .filter(relation -> relation.getId().equals(relationId))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}