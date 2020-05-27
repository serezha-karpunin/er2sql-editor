package com.etu.infrastructure.workflow.strategy.erm.relation;

import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.er.ERModelService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelation;
import com.etu.ui.UiFactory;
import com.etu.ui.UiStageBuilder;
import com.etu.ui.ermodel.relation.editor.ERModelRelationEditorView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.localization.LocalizationConstants.ERM_RELATION_EDITOR_TITLE_PROPERTY_NAME;
import static com.etu.infrastructure.workflow.service.WorkflowType.ERM_EDIT_RELATION;

@Component
public class ERModelEditRelationWorkflowStrategy implements ERModelRelationWorkflowStrategy {

    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private ERModelService erModelFacade;

    @Autowired
    @Qualifier("ERModelRemoveRelationWorkflowStrategy")
    private ERModelRelationWorkflowStrategy erModelRemoveRelationWorkflowStrategy;

    @Override
    public void execute(ERModelRelation relationDto) {
        ERModelRelationEditorView editorView = uiFactory.createView(ERModelRelationEditorView.class);

        String title = localizationService.getLocalizedString(ERM_RELATION_EDITOR_TITLE_PROPERTY_NAME);
        Stage editorStage = new UiStageBuilder()
                .withTitle(title)
                .withModality(Modality.APPLICATION_MODAL)
                .withView(editorView.getRootNode())
                .build();

        editorView.getMediator().initEditor(relationDto, dto -> {
                    erModelFacade.saveRelation(relationDto);
                    editorStage.close();
                },
                dto -> {
                    erModelRemoveRelationWorkflowStrategy.execute(relationDto);
                    editorStage.close();

                }, editorStage::close
        );

        editorStage.showAndWait();
    }

    @Override
    public WorkflowType getWorkflowType() {
        return ERM_EDIT_RELATION;
    }
}
