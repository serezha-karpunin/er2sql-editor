package com.etu.ui.rmodel.tab;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.EventTypes;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.ui.AbstractMediator;
import com.etu.ui.UiFactory;
import com.etu.ui.rmodel.canvas.RModelCanvasView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.workflow.service.WorkflowType.*;

@Component
@Scope("prototype")
public class RModelTabMediator extends AbstractMediator {

    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private EventFacade eventFacade;

    private BooleanProperty linkCreationStarted = new SimpleBooleanProperty(false);

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(EventTypes.RM_LINK_CREATION_STARTED, this::handleLinkCreationStarted);
        eventFacade.addEventListener(EventTypes.RM_LINK_CREATION_FINISHED, this::handleLinkCreationFinished);
    }

    public void startSqlGenerationWorkflow() {
        workflowService.startProjectWorkflow(SQL_GENERATION);
    }

    public RModelCanvasView createCanvas() {
        return uiFactory.createView(RModelCanvasView.class);
    }

    public BooleanProperty linkCreationStartedProperty() {
        return linkCreationStarted;
    }

    private void handleLinkCreationStarted(Event event) {
        linkCreationStarted.set(true);
    }

    private void handleLinkCreationFinished(Event event) {
        linkCreationStarted.set(false);
    }

    public void startAddRelationWorkflow() {
//        workflowService.startRModelRelationWorkflow(RM_ADD_RELATION, new RModelRelation());
    }

    public void startAddLinkWorkflow() {
//        workflowService.startRModelLinkWorkflow(RM_ADD_LINK, new RModelLink());
    }

    public void finishLinkCreation() {
        workflowService.startProjectWorkflow(RM_CONFIRM_LINK_CREATION);
    }

    public void cancelLinkCreation() {
        workflowService.startProjectWorkflow(RM_CANCEL_LINK_CREATION);
    }
}
