package com.etu.infrastructure.workflow.strategy.erm.relation;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.ERModelEntityEvent;
import com.etu.infrastructure.event.dto.ERModelRelationEvent;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.erm.canvas.ERModelDiscardCanvasSelectionWorkflowStrategy;
import com.etu.model.er.ERModelService;
import javafx.event.Event;
import javafx.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.etu.infrastructure.event.dto.EventTypes.*;

public abstract class ERModelAddRelationWorkflowStrategy implements ERModelRelationWorkflowStrategy {

    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private ERModelService erModelFacade;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ERModelDiscardCanvasSelectionWorkflowStrategy erModelDiscardCanvasSelectionWorkflowStrategy;

    private ERModelRelationType relationType;
    private WorkflowType workflowType;

    private List<ERModelEntity> selectedEntities = new ArrayList<>();

    private EventHandler<ERModelEntityEvent> entitySelectedHandler = this::handleEntitySelected;
    private EventHandler<ERModelEntityEvent> entityDeselectedHandler = this::handleEntityDeselected;
    private EventHandler<Event> relationCreationConfirmedHandler = this::handleRelationCreationConfirmed;
    private EventHandler<Event> relationCreationCancelledHandler = this::handleRelationCreationCancelled;

    @Autowired
    public ERModelAddRelationWorkflowStrategy(ERModelRelationType relationType, WorkflowType workflowType) {
        this.relationType = relationType;
        this.workflowType = workflowType;
    }

    @Override
    public void execute(ERModelRelation relationDto) {
        erModelDiscardCanvasSelectionWorkflowStrategy.execute();

        eventFacade.addEventListener(EventTypes.ERM_ENTITY_SELECTED_TO_CREATE_RELATION, entitySelectedHandler);
        eventFacade.addEventListener(EventTypes.ERM_ENTITY_DESELECTED_TO_CREATE_RELATION, entityDeselectedHandler);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_CONFIRMED, relationCreationConfirmedHandler);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_CANCELLED, relationCreationCancelledHandler);

        eventFacade.fireEvent(new Event(ERM_RELATION_CREATION_STARTED));
    }

    private void handleEntitySelected(ERModelEntityEvent event) {
        ERModelEntity entity = event.getEntity();
        selectedEntities.add(entity);
    }

    private void handleEntityDeselected(ERModelEntityEvent event) {
        ERModelEntity entity = event.getEntity();
        selectedEntities.remove(entity);
    }

    private void handleRelationCreationConfirmed(Event event) {
        if (!validateSelectedEntities()) {
            return;
        }

        ERModelRelation ermRelationDto = createRelation();

        ERModelRelation savedRelation = erModelFacade.saveRelation(ermRelationDto);
        eventFacade.fireEvent(new ERModelRelationEvent(ERM_RELATION_CREATED, savedRelation));

        cleanUp();
    }

    protected abstract boolean validateSelectedEntities();

    protected abstract ERModelRelation createRelation();

    private void removeListeners() {
        eventFacade.removeEventListener(EventTypes.ERM_ENTITY_SELECTED_TO_CREATE_RELATION, entitySelectedHandler);
        eventFacade.removeEventListener(EventTypes.ERM_RELATION_CREATION_CANCELLED, relationCreationCancelledHandler);
    }

    private void handleRelationCreationCancelled(Event event) {
        cleanUp();
    }

    private void cleanUp() {
        removeListeners();

        selectedEntities.clear();
        eventFacade.fireEvent(new Event(ERM_RELATION_CREATION_FINISHED));

        workflowService.startProjectWorkflow(WorkflowType.ERM_DISCARD_CANVAS_SELECTION);
    }

    protected ERModelRelationType getRelationType() {
        return relationType;
    }

    protected List<ERModelEntity> getSelectedEntities() {
        return selectedEntities;
    }

    @Override
    public WorkflowType getWorkflowType() {
        return workflowType;
    }
}
