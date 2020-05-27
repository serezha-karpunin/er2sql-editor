package com.etu.infrastructure.workflow.strategy.erm.entity;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.ERModelEntityEvent;
import com.etu.infrastructure.event.dto.ERModelRelationEvent;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.er.ERModelService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.ERM_ENTITY_REMOVED;
import static com.etu.infrastructure.event.dto.EventTypes.ERM_RELATION_REMOVED;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_REMOVE_ENTITY;

@Component
public class ERModelRemoveEntityWorkflowStrategy implements ERModelEntityWorkflowStrategy {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ERModelService erModelService;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute(ERModelEntity entity) {
        applicationContext.getBean("removeErmEntityAlert", Alert.class).showAndWait()
                .map(ButtonType::getButtonData)
                .ifPresent(result -> handleAlertResult(result, entity));
    }

    private void handleAlertResult(ButtonBar.ButtonData alertResult, ERModelEntity entity) {
        switch (alertResult) {
            case YES:
                doRemove(entity);
                break;
            case CANCEL_CLOSE:
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void doRemove(ERModelEntity entity) {
        erModelService.getRelationsFor(entity)
                .forEach(this::removeRelation);

        removeEntity(entity);
    }

    private void removeRelation(ERModelRelation relation) {
        erModelService.removeRelation(relation);
        eventFacade.fireEvent(new ERModelRelationEvent(ERM_RELATION_REMOVED, relation));
    }

    private void removeEntity(ERModelEntity entity) {
        erModelService.removeEntity(entity);
        eventFacade.fireEvent(new ERModelEntityEvent(ERM_ENTITY_REMOVED, entity));
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_REMOVE_ENTITY;
    }
}
