package com.etu.infrastructure.state.convert.erm;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelEntity;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelEntityAttribute;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableList;

@Component
public class ERModelEntityFactory {

    public List<ERModelEntity> create(List<SerializableERModelEntity> sources) {
        return sources.stream()
                .map(this::create)
                .collect(toList());
    }

    private ERModelEntity create(SerializableERModelEntity source) {
        ERModelEntity target = new ERModelEntity();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLayoutX(source.getX());
        target.setLayoutY(source.getY());
        target.setAttributes(observableList(createAttributes(source.getAttributes())));

        return target;
    }

    private List<ERModelEntityAttribute> createAttributes(List<SerializableERModelEntityAttribute> sources) {
        return sources.stream()
                .map(this::createAttribute)
                .collect(toList());
    }

    private ERModelEntityAttribute createAttribute(SerializableERModelEntityAttribute source) {
        ERModelEntityAttribute target = new ERModelEntityAttribute();
        target.setId(source.getId());
        target.setKey(source.isKey());
        target.setName(source.getName());

        return target;
    }
}
