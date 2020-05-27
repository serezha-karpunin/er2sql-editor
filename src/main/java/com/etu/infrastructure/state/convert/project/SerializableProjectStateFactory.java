package com.etu.infrastructure.state.convert.project;

import com.etu.infrastructure.state.convert.erm.SerializableERModelEntityFactory;
import com.etu.infrastructure.state.convert.erm.SerializableERModelRelationFactory;
import com.etu.infrastructure.state.convert.rm.SerializableRModelLinkFactory;
import com.etu.infrastructure.state.convert.rm.SerializableRModelRelationFactory;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.state.dto.runtime.project.ProjectState;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelState;
import com.etu.infrastructure.state.dto.serializable.project.SerializableProjectState;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SerializableProjectStateFactory {

    @Autowired
    private SerializableERModelEntityFactory serializableERModelEntityFactory;
    @Autowired
    private SerializableERModelRelationFactory serializableERModelRelationFactory;

    @Autowired
    private SerializableRModelRelationFactory serializableRModelRelationFactory;
    @Autowired
    private SerializableRModelLinkFactory serializableRModelLinkFactory;

    public SerializableProjectState create(ProjectState source) {
        SerializableProjectState target = new SerializableProjectState();

        target.setProjectName(source.getProjectName());
        target.setErModelState(createErModelState(source.getErModelState()));
        target.setRModelState(createRModelState(source.getRModelState()));

        return target;
    }

    private SerializableERModelState createErModelState(ERModelState state) {
        SerializableERModelState serializableState = new SerializableERModelState();
        serializableState.setEntities(serializableERModelEntityFactory.create(state.getEntities()));
        serializableState.setRelations(serializableERModelRelationFactory.create(state.getRelations()));

        return serializableState;
    }

    private SerializableRModelState createRModelState(RModelState source) {
        SerializableRModelState target = new SerializableRModelState();
        target.setRelations(serializableRModelRelationFactory.create(source.getRelations()));
        target.setLinks(serializableRModelLinkFactory.create(source.getLinks()));

        return target;
    }
}
