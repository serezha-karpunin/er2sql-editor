package com.etu.model.er;

import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Component
public class ERModelServiceImpl implements ERModelService {
    @Autowired
    private ProjectStateService projectStateService;

    @Override
    public ERModelEntity saveEntity(ERModelEntity entity) {
        if (entity.getId() == null) {
            entity.setId(randomUUID().toString());
        }

        entity.getAttributes().stream()
                .filter(attribute -> attribute.getId() == null)
                .forEach(attribute -> attribute.setId(UUID.randomUUID().toString()));

        List<ERModelEntity> entities = projectStateService.getProjectState().getErModelState().getEntities();
        if (!entities.contains(entity)) {
            entities.add(entity);
        }

        return entity;
    }

    @Override
    public ERModelRelation saveRelation(ERModelRelation relation) {
        if (relation.getId() == null) {
            relation.setId(randomUUID().toString());
        }

        relation.getRelationSides().stream()
                .filter(side -> side.getId() == null)
                .forEach(side -> side.setId(UUID.randomUUID().toString()));

        List<ERModelRelation> relations = projectStateService.getProjectState().getErModelState().getRelations();
        if (!relations.contains(relation)) {
            relations.add(relation);
        }

        return relation;
    }

    @Override
    public List<ERModelEntity> getCurrentEntities() {
        return projectStateService.getProjectState().getErModelState().getEntities();
    }

    @Override
    public List<ERModelRelation> getCurrentRelations() {
        return projectStateService.getProjectState().getErModelState().getRelations();
    }

    @Override
    public void removeEntity(ERModelEntity entity) {
        getCurrentEntities().remove(entity);
    }

    @Override
    public void removeRelation(ERModelRelation relation) {
        getCurrentRelations().remove(relation);
    }

    @Override
    public List<ERModelRelation> getRelationsFor(ERModelEntity entity) {
        return getCurrentRelations().stream()
                .filter(hasRelationSideFor(entity))
                .collect(toList());
    }

    @Override
    public ERModelState getState() {
        return projectStateService.getProjectState().getErModelState();
    }

    private Predicate<ERModelRelation> hasRelationSideFor(ERModelEntity entity) {
        return relation -> relation.getRelationSides().stream()
                .anyMatch(side -> Objects.equals(side.getEntity(), entity));
    }
}

