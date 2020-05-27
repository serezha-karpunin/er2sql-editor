package com.etu.model.relational;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;

import java.util.List;

public interface RModelService {
    RModelRelation saveRelation(RModelRelation table);

    RModelLink saveLink(RModelLink relation);

    List<RModelRelation> getCurrentRelations();

    List<RModelLink> getCurrentLinks();

    void setRModelState(RModelState rmState);

    List<RModelLink> getLinksFor(RModelRelation relation);

    void removeRelation(RModelRelation relation);

    void removeLink(RModelLink link);
}
