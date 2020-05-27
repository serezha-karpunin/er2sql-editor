package com.etu.infrastructure.workflow.strategy.erm.relation;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.ERModelRelationEvent;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.er.ERModelService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.ERM_RELATION_REMOVED;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_REMOVE_RELATION;

@Component
public class ERModelRemoveRelationWorkflowStrategy implements ERModelRelationWorkflowStrategy {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ERModelService erModelFacade;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute(ERModelRelation relation) {
        applicationContext.getBean("removeErmRelationAlert", Alert.class).showAndWait()
                .map(ButtonType::getButtonData)
                .ifPresent(result -> handleAlertResult(result, relation));
    }

    private void handleAlertResult(ButtonBar.ButtonData alertResult, ERModelRelation relation) {
        switch (alertResult) {
            case YES:
                doRemove(relation);
                break;
            case CANCEL_CLOSE:
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void doRemove(ERModelRelation relation) {
        erModelFacade.removeRelation(relation);
        eventFacade.fireEvent(new ERModelRelationEvent(ERM_RELATION_REMOVED, relation));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_REMOVE_RELATION;
    }
}
