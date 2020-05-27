package com.etu.infrastructure.workflow.strategy.transform;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.RModelLinkEvent;
import com.etu.infrastructure.event.dto.RModelRelationEvent;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.transform.converter.TransformationStateConverter;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;
import com.etu.infrastructure.workflow.strategy.transform.step.entity.TransformEntityStep;
import com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformDependencyRelationStep;
import com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformManyToManyRelationStep;
import com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformOneToOneAllMandatoryRelationStep;
import com.etu.infrastructure.workflow.strategy.transform.step.relation.TransformOneToOneSomeMandatoryRelationStep;
import com.etu.model.er.ERModelService;
import com.etu.model.relational.RModelService;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.etu.infrastructure.event.dto.EventTypes.*;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_TRANSFORM;

@Component
public class ERModelTransformWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private ERModelService erModelService;
    @Autowired
    private RModelService rModelService;

    @Autowired
    private TransformEntityStep transformEntityStep;
    @Autowired
    private TransformDependencyRelationStep transformDependencyRelationStep;
    @Autowired
    private TransformOneToOneAllMandatoryRelationStep transformOneToOneAllMandatoryRelationStep;
    @Autowired
    private TransformOneToOneSomeMandatoryRelationStep transformOneToOneSomeMandatoryRelationStep;

    @Autowired
    private TransformManyToManyRelationStep transformManyToManyRelationStep;

    @Autowired
    private TransformationStateConverter transformationStateConverter;

    @Override
    public void execute() {
        cleanUp();

        ERModelState ermState = erModelService.getState();

        RModelState rmState = transform(ermState);

        rModelService.setRModelState(rmState);
        eventFacade.fireEvent(new Event(RM_GENERATED));
    }

    private void cleanUp() {
        List<RModelRelation> relationsToRemove = new ArrayList<>(rModelService.getCurrentRelations());

        relationsToRemove
                .forEach(this::doRemove);
    }

    private void doRemove(RModelRelation relation) {
        rModelService.getLinksFor(relation)
                .forEach(this::removeLink);

        removeRelation(relation);
    }

    private void removeLink(RModelLink link) {
        rModelService.removeLink(link);
        eventFacade.fireEvent(new RModelLinkEvent(RM_LINK_REMOVED, link));
    }

    private void removeRelation(RModelRelation relation) {
        rModelService.removeRelation(relation);
        eventFacade.fireEvent(new RModelRelationEvent(RM_RELATION_REMOVED, relation));
    }

    private RModelState transform(ERModelState ermState) {
        TransformationState tState = new TransformationState();

        applyTransformationRules(ermState, tState);

        return transformationStateConverter.convert(tState);
    }

    private void applyTransformationRules(ERModelState source, TransformationState target) {
        // TODO: 17.05.2020 change comments, add all other relations
        // 1. Create transformation entities on the basis of ER-model entities
        transformEntityStep.process(source, target);

        transformDependencyRelationStep.process(source, target);

        // 2. Handle One-To-One relations when both relation sides are mandatory
        // There are two ways to process such relation:
        //   1.) Merge entities to single one
        //   2.) Don't merge them, create a link instead
        transformOneToOneAllMandatoryRelationStep.process(source, target);

        // 3. Handle One-To-One relations when one relation side isn't mandatory
        transformOneToOneSomeMandatoryRelationStep.process(source, target);

        transformManyToManyRelationStep.process(source, target);
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_TRANSFORM;
    }
}
