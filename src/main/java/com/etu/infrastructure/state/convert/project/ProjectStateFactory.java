package com.etu.infrastructure.state.convert.project;

import com.etu.infrastructure.state.convert.erm.ERModelEntityFactory;
import com.etu.infrastructure.state.convert.erm.ERModelRelationFactory;
import com.etu.infrastructure.state.convert.rm.RModelLinkFactory;
import com.etu.infrastructure.state.convert.rm.RModelRelationFactory;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.state.dto.runtime.project.ProjectState;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.state.dto.serializable.erm.SerializableERModelState;
import com.etu.infrastructure.state.dto.serializable.project.SerializableProjectState;
import com.etu.infrastructure.state.dto.serializable.rm.SerializableRModelState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectStateFactory {
    @Autowired
    private ERModelEntityFactory erModelEntityFactory;
    @Autowired
    private ERModelRelationFactory erModelRelationFactory;
    @Autowired
    private RModelRelationFactory rModelRelationFactory;
    @Autowired
    private RModelLinkFactory rModelLinkFactory;

    public ProjectState create(SerializableProjectState source) {
        ProjectState target = new ProjectState();

        target.setProjectName(source.getProjectName());
        target.setErModelState(createErModelState(source.getErModelState()));
        target.setRModelState(createRModelState(source.getRModelState()));

        return target;
    }

    private ERModelState createErModelState(SerializableERModelState source) {
        ERModelState target = new ERModelState();
        target.setEntities(erModelEntityFactory.create(source.getEntities()));
        target.setRelations(erModelRelationFactory.create(source.getRelations(), target));

        return target;
    }

    private RModelState createRModelState(SerializableRModelState source) {
        RModelState target = new RModelState();
        target.setRelations(rModelRelationFactory.create(source.getRelations(), target));
        target.setLinks(rModelLinkFactory.create(source.getLinks(), target));

        return target;
    }
}
