package com.etu.infrastructure.state.convert.erm;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelRelation;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelRelationSide;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SerializableERModelRelationFactory {

    public List<SerializableERModelRelation> create(List<ERModelRelation> sources) {
        return sources.stream()
                .map(this::create)
                .collect(toList());
    }

    private SerializableERModelRelation create(ERModelRelation source) {
        SerializableERModelRelation target = new SerializableERModelRelation();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setRelationType(source.getRelationType());
        target.setRelationSides(createRelationSides(source.getRelationSides()));

        return target;
    }

    private List<SerializableERModelRelationSide> createRelationSides(List<ERModelRelationSide> sources) {
        return sources.stream()
                .map(this::createRelationSide)
                .collect(toList());
    }

    private SerializableERModelRelationSide createRelationSide(ERModelRelationSide source) {
        SerializableERModelRelationSide target = new SerializableERModelRelationSide();
        target.setId(source.getId());
        target.setEntityId(source.getEntity().getId());
        target.setMandatory(source.isMandatory());
        target.setType(source.getType());

        return target;
    }
}
