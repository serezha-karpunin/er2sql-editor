package com.etu.infrastructure.workflow.strategy.transform.step.relation;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntity;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationEntityAttribute;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformationUtils.*;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.ONE;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.ONE_TO_ONE;
import static java.util.stream.Collectors.toList;

@Component
public class TransformOneToOneAllMandatoryRelationStep extends AbstractTransformRelationStep {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected boolean canApply(ERModelRelation relation) {
        return hasType(relation, ONE_TO_ONE)
                && allSidesMatch(relation, side -> hasType(side, ONE) && hasMandatory(side, true));
    }

    @Override
    protected void process(ERModelState source, TransformationState target, ERModelRelation relation) {
        List<TransformationEntity> entities = getTransformationEntitiesFor(relation, target);


        Alert alert = applicationContext.getBean("oneToOneRelationDecisionAlert", Alert.class);
        alert.setHeaderText(alert.getHeaderText() + " " + entities.get(0).getName() + " " + entities.get(1).getName());
        alert.showAndWait()
                .map(ButtonType::getButtonData)
                .ifPresent(result -> handleAlertResult(result, source, target, relation, entities));
    }


    private void handleAlertResult(ButtonData result, ERModelState source, TransformationState target, ERModelRelation relation, List<TransformationEntity> entities) {
        switch (result) {
            case YES:
                merge(source, target, relation, entities);
                break;
            case NO:
                createRelation(source, target, relation, entities);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void merge(ERModelState source, TransformationState target, ERModelRelation relation, List<TransformationEntity> entities) {
        target.getEntities().removeAll(entities);

        TransformationEntity mergedEntity = mergeEntities(relation, entities);

        target.getEntities().add(mergedEntity);
    }

    private TransformationEntity mergeEntities(ERModelRelation relation, List<TransformationEntity> entities) {

        TransformationEntity entity = new TransformationEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(getNameForNewEntity(relation, entities));
        entity.setSourceEntities(getSourceEntitiesForMergedEntity(entities));
        entity.setAttributes(getAttributesForMergedEntity(entities));

        return entity;
    }

    private List<ERModelEntity> getSourceEntitiesForMergedEntity(List<TransformationEntity> entities) {
        return entities.stream()
                .map(TransformationEntity::getSourceEntities)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<TransformationEntityAttribute> getAttributesForMergedEntity(List<TransformationEntity> entities) {
        return entities.stream()
                .map(TransformationEntity::getAttributes)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private void createRelation(ERModelState source, TransformationState target, ERModelRelation relation, List<TransformationEntity> entities) {
        // TODO: 03.05.2020
        throw new UnsupportedOperationException();
    }
}
