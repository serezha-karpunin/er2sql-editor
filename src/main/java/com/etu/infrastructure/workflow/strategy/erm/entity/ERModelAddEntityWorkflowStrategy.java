package com.etu.infrastructure.workflow.strategy.erm.entity;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.event.dto.ERModelEntityEvent;
import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.er.ERModelService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntity;
import com.etu.ui.UiFactory;
import com.etu.ui.UiStageBuilder;
import com.etu.ui.ermodel.entity.editor.ERModelEntityEditorView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.etu.infrastructure.event.dto.EventTypes.ERM_ENTITY_CREATED;
import static com.etu.infrastructure.localization.LocalizationConstants.ERM_ENTITY_EDITOR_TITLE_PROPERTY_NAME;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_ADD_ENTITY;

@Component
public class ERModelAddEntityWorkflowStrategy implements ERModelEntityWorkflowStrategy {
    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private ERModelService erModelFacade;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute(ERModelEntity entityDto) {
        ERModelEntityEditorView editorView = uiFactory.createView(ERModelEntityEditorView.class);

        String title = localizationService.getLocalizedString(ERM_ENTITY_EDITOR_TITLE_PROPERTY_NAME);
        Stage editorStage = new UiStageBuilder()
                .withTitle(title)
                .withModality(Modality.APPLICATION_MODAL)
                .withView(editorView.getRootNode())
                .build();

        editorView.getMediator().configureEditor(entityDto, getSuccessHandler(editorStage), editorStage::close);
        editorStage.showAndWait();
    }

    private Consumer<ERModelEntity> getSuccessHandler(Stage editorStage) {
        return entityToSave -> {
            ERModelEntity savedEntity = erModelFacade.saveEntity(entityToSave);
            eventFacade.fireEvent(new ERModelEntityEvent(ERM_ENTITY_CREATED, savedEntity));

            editorStage.close();
        };
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_ADD_ENTITY;
    }
}
