package com.etu.infrastructure.state.convert.erm;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelEntity;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelEntityAttribute;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SerializableERModelEntityFactory {

    public List<SerializableERModelEntity> create(List<ERModelEntity> sources) {
        return sources.stream()
                .map(this::create)
                .collect(toList());
    }

    private SerializableERModelEntity create(ERModelEntity source) {
        SerializableERModelEntity target = new SerializableERModelEntity();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setX(source.getLayoutX());
        target.setY(source.getLayoutY());
        target.setAttributes(createAttributes(source.getAttributes()));

        return target;
    }

    private List<SerializableERModelEntityAttribute> createAttributes(List<ERModelEntityAttribute> sources) {
        return sources.stream()
                .map(this::createAttribute)
                .collect(toList());
    }

    private SerializableERModelEntityAttribute createAttribute(ERModelEntityAttribute source) {
        SerializableERModelEntityAttribute target = new SerializableERModelEntityAttribute();
        target.setId(source.getId());
        target.setKey(source.isKey());
        target.setName(source.getName());

        return target;
    }
}
