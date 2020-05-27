package com.etu.infrastructure.workflow.strategy.transform.step.entity;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.workflow.strategy.transform.step.TransformStep;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
public class TransformEntityStep implements TransformStep {
    @Override
    public void process(ERModelState source, TransformationState target) {
        List<TransformationEntity> entities = source.getEntities().stream()
                .map(this::createTransformationEntity)
                .collect(toList());

        target.getEntities().addAll(entities);
    }

    private TransformationEntity createTransformationEntity(ERModelEntity ermEntity) {
        TransformationEntity entity = new TransformationEntity();

        entity.setId(UUID.randomUUID().toString());
        entity.setName(ermEntity.getName());
        entity.setSourceEntities(Arrays.asList(ermEntity));

        for (ERModelEntityAttribute ermAttribute : ermEntity.getAttributes()) {
            TransformationEntityAttribute attribute = new TransformationEntityAttribute();
            attribute.setId(UUID.randomUUID().toString());
            attribute.setKey(ermAttribute.isKey());
            attribute.setName(ermAttribute.getName());

            entity.getAttributes().add(attribute);
        }

        return entity;
    }
}
