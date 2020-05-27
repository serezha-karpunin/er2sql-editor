package com.etu.ui.ermodel.tab;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.ui.AbstractMediator;
import com.etu.ui.UiFactory;
import com.etu.ui.ermodel.canvas.ERModelCanvasView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ERModelTabMediator extends AbstractMediator {

    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private EventFacade eventFacade;

    private BooleanProperty relationCreationStarted = new SimpleBooleanProperty(false);

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_STARTED, this::handleRelationCreationStarted);
        eventFacade.addEventListener(EventTypes.ERM_RELATION_CREATION_FINISHED, this::handleRelationCreationFinished);
    }

    public void startAddEntityWorkflow() {
        workflowService.startErModelEntityWorkflow(WorkflowType.ERM_ADD_ENTITY, new ERModelEntity());
    }

    public void startAddOneToOneRelationWorkflow() {
        workflowService.startErModelRelationWorkflow(WorkflowType.ERM_ADD_ONE_TO_ONE_RELATION, new ERModelRelation());
    }

    public void startAddOneToManyRelationWorkflow() {
        workflowService.startErModelRelationWorkflow(WorkflowType.ERM_ADD_ONE_TO_MANY_RELATION, new ERModelRelation());
    }

    public void startAddManyToManyRelationWorkflow() {
        workflowService.startErModelRelationWorkflow(WorkflowType.ERM_ADD_MANY_TO_MANY_RELATION, new ERModelRelation());
    }

    public void startAddDependencyRelationWorkflow() {
        workflowService.startErModelRelationWorkflow(WorkflowType.ERM_ADD_DEPENDENCY_RELATION, new ERModelRelation());
    }

    public void startAddCategoryRelationWorkflow() {
        workflowService.startErModelRelationWorkflow(WorkflowType.ERM_ADD_CATEGORY_RELATION, new ERModelRelation());
    }

    public void finishRelationCreation() {
        workflowService.startProjectWorkflow(WorkflowType.ERM_CONFIRM_RELATION_CREATION);
    }

    public void cancelRelationCreation() {
        workflowService.startProjectWorkflow(WorkflowType.ERM_CANCEL_RELATION_CREATION);
    }

    public void startTransformationWorkflow() {
        workflowService.startProjectWorkflow(WorkflowType.ERM_TRANSFORM);
    }

    public ERModelCanvasView createCanvas() {
        return uiFactory.createView(ERModelCanvasView.class);
    }

    public BooleanProperty relationCreationStartedProperty() {
        return relationCreationStarted;
    }

    private void handleRelationCreationStarted(Event event) {
        relationCreationStarted.set(true);
    }

    private void handleRelationCreationFinished(Event event) {
        relationCreationStarted.set(false);
    }
}
