package com.etu.model.er;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;

import java.util.List;

public interface ERModelService {
    ERModelEntity saveEntity(ERModelEntity entityDto);

    ERModelRelation saveRelation(ERModelRelation relationDto);

    List<ERModelEntity> getCurrentEntities();

    List<ERModelRelation> getCurrentRelations();

    void removeEntity(ERModelEntity entity);

    void removeRelation(ERModelRelation relation);

    List<ERModelRelation> getRelationsFor(ERModelEntity entity);

    ERModelState getState();
}
