package com.etu.infrastructure.state.convert.rm;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSide;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelLink;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelLinkSide;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class SerializableRModelLinkFactory {
    public List<SerializableRModelLink> create(List<RModelLink> sources) {
        return sources.stream()
                .map(this::create)
                .collect(toList());
    }

    private SerializableRModelLink create(RModelLink source) {
        SerializableRModelLink target = new SerializableRModelLink();
        target.setId(source.getId());
        target.setType(source.getRelationType());
        target.setLinkSideFrom(createLinkSide(source.getLinkSideFrom()));
        target.setLinkSideTo(createLinkSide(source.getLinkSideTo()));
        target.setLinkedAttributesMap(createAttributesMap(source.getLinkedAttributesMap()));

        return target;
    }

    private Map<String, String> createAttributesMap(Map<RModelRelationAttribute, RModelRelationAttribute> source) {
        Map<String, String> target = new HashMap<>();
        source.forEach((k, v) -> target.put(k.getId(), v.getId()));

        return target;
    }

    private SerializableRModelLinkSide createLinkSide(RModelLinkSide source) {
        SerializableRModelLinkSide target = new SerializableRModelLinkSide();
        target.setId(source.getId());
        target.setRelationId(source.getRelation().getId());
        target.setType(source.getType());

        return target;
    }
}
