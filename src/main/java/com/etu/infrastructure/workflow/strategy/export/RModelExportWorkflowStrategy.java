package com.etu.infrastructure.workflow.strategy.export;

import com.etu.infrastructure.PrimaryStageProvider;
import com.etu.infrastructure.export.ExportService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.etu.infrastructure.workflow.service.WorkflowType.EXPORT_RM;

@Component
public class RModelExportWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PrimaryStageProvider primaryStageProvider;
    @Autowired
    private FileChooser exportRelationalModelFileChooser;
    @Autowired
    private ExportService exportService;

    @Override
    public void execute() {
        Stage primaryStage = primaryStageProvider.getPrimaryStage();
        File file = exportRelationalModelFileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            Region content = (Region) primaryStage.getScene().lookup("#rmCanvasContent");
            exportService.exportAsImage(content, file);
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return EXPORT_RM;
    }
}
