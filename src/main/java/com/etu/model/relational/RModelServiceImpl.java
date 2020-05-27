package com.etu.model.relational;

import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.state.dto.runtime.rm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Component
public class RModelServiceImpl implements RModelService {

    @Autowired
    private ProjectStateService projectStateService;

    @Override
    public RModelRelation saveRelation(RModelRelation relation) {
        if (relation.getId() == null) {
            relation.setId(randomUUID().toString());
        }

        relation.getAttributes()
                .forEach(attribute -> saveRelationAttribute(attribute, relation));

        List<RModelRelation> relations = projectStateService.getProjectState().getRModelState().getRelations();
        if (!relations.contains(relation)) {
            relations.add(relation);
        }

        return relation;
    }

    private void saveRelationAttribute(RModelRelationAttribute attribute, RModelRelation relation) {
        if (attribute.getId() == null) {
            attribute.setId(UUID.randomUUID().toString());
        }
        if (attribute.getRelation() == null) {
            throw new IllegalStateException();
        }
    }

    @Override
    public RModelLink saveLink(RModelLink link) {
        if (link.getId() == null) {
            link.setId(randomUUID().toString());
        }

        RModelLinkSide linkSideFrom = link.getLinkSideFrom();
        if (linkSideFrom.getId() == null) {
            linkSideFrom.setId(UUID.randomUUID().toString());
        }

        RModelLinkSide linkSideTo = link.getLinkSideTo();
        if (linkSideTo.getId() == null) {
            linkSideTo.setId(UUID.randomUUID().toString());
        }

        List<RModelLink> links = projectStateService.getProjectState().getRModelState().getLinks();
        if (!links.contains(link)) {
            links.add(link);
        }

        return link;
    }

    @Override
    public List<RModelRelation> getCurrentRelations() {
        return projectStateService.getProjectState().getRModelState().getRelations();
    }

    @Override
    public List<RModelLink> getCurrentLinks() {
        return projectStateService.getProjectState().getRModelState().getLinks();
    }

    @Override
    public void setRModelState(RModelState rmState) {
        projectStateService.getProjectState().setRModelState(rmState);
    }

    @Override
    public List<RModelLink> getLinksFor(RModelRelation relation) {
        return getCurrentLinks().stream()
                .filter(hasLinkSideFor(relation))
                .collect(toList());
    }

    @Override
    public void removeRelation(RModelRelation relation) {
        getCurrentRelations().remove(relation);
    }

    @Override
    public void removeLink(RModelLink link) {
        getCurrentLinks().remove(link);
    }

    private Predicate<RModelLink> hasLinkSideFor(RModelRelation relation) {
        return link -> Objects.equals(link.getLinkSideFrom().getRelation().getId(), relation.getId())
                || Objects.equals(link.getLinkSideTo().getRelation().getId(), relation.getId());
    }
}
