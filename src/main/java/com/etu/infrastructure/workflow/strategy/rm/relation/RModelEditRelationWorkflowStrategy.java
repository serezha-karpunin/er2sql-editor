package com.etu.infrastructure.workflow.strategy.rm.relation;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.model.relational.RModelService;
import com.etu.ui.UiFactory;
import com.etu.ui.UiStageBuilder;
import com.etu.ui.rmodel.relation.editor.RModelRelationEditorView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.etu.infrastructure.localization.LocalizationConstants.RM_RELATION_EDITOR_TITLE_PROPERTY_NAME;
import static com.etu.infrastructure.workflow.service.WorkflowType.RM_EDIT_RELATION;

@Component
public class RModelEditRelationWorkflowStrategy implements RModelRelationWorkflowStrategy {
    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private RModelService rModelService;
    @Autowired
    private EventFacade eventFacade;

    @Override
    public void execute(RModelRelation relation) {
        RModelRelationEditorView editorView = uiFactory.createView(RModelRelationEditorView.class);

        String title = localizationService.getLocalizedString(RM_RELATION_EDITOR_TITLE_PROPERTY_NAME);
        Stage editorStage = new UiStageBuilder()
                .withTitle(title)
                .withModality(Modality.APPLICATION_MODAL)
                .withView(editorView.getRootNode())
                .build();

        editorView.getMediator().configureEditor(relation, getSuccessHandler(editorStage), editorStage::close);
        editorStage.showAndWait();
    }

    private Consumer<RModelRelation> getSuccessHandler(Stage editorStage) {
        return relationToSave -> {
            rModelService.saveRelation(relationToSave);
            editorStage.close();
        };
    }

    @Override
    public WorkflowType getWorkflowType() {
        return RM_EDIT_RELATION;
    }
}
