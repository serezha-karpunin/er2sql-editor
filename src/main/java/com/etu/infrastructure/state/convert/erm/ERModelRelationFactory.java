package com.etu.infrastructure.state.convert.erm;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelRelation;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelRelationSide;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ERModelRelationFactory {
    public List<ERModelRelation> create(List<SerializableERModelRelation> sources, ERModelState state) {
        return sources.stream()
                .map(source -> create(source, state))
                .collect(toList());
    }

    private ERModelRelation create(SerializableERModelRelation source, ERModelState state) {
        ERModelRelation relationDto = new ERModelRelation();
        relationDto.setId(source.getId());
        relationDto.setName(source.getName());
        relationDto.setRelationType(source.getRelationType());
        relationDto.setRelationSides(FXCollections.observableList(createRelationSides(source.getRelationSides(), state)));

        return relationDto;
    }

    private List<ERModelRelationSide> createRelationSides(List<SerializableERModelRelationSide> source, ERModelState state) {
        return source.stream()
                .map(side -> createRelationSide(side, state))
                .collect(toList());
    }

    private ERModelRelationSide createRelationSide(SerializableERModelRelationSide source, ERModelState state) {
        ERModelRelationSide target = new ERModelRelationSide();
        target.setId(source.getId());
        target.setEntity(getEntityForId(source.getEntityId(), state));
        target.setMandatory(source.isMandatory());
        target.setType(source.getType());

        return target;
    }

    private ERModelEntity getEntityForId(String entityId, ERModelState state) {
        return state.getEntities().stream()
                .filter(entity -> entity.getId().equals(entityId))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
