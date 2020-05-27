package com.etu.infrastructure.workflow.strategy.export;

import com.etu.infrastructure.PrimaryStageProvider;
import com.etu.infrastructure.export.ExportService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.etu.infrastructure.workflow.service.WorkflowType.EXPORT_GENERATED_SQL;

@Component
public class SqlExportWorkflowStrategy implements SimpleWorkflowStrategy {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PrimaryStageProvider primaryStageProvider;
    @Autowired
    private FileChooser exportSqlFileChooser;
    @Autowired
    private ExportService exportService;

    @Override
    public void execute() {
        Stage primaryStage = primaryStageProvider.getPrimaryStage();
        File file = exportSqlFileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            TextArea textArea = (TextArea) primaryStage.getScene().lookup("#sqlTextArea");
            String sqlToExport = textArea.getText();
            exportService.exportSql(sqlToExport, file);
        }
    }

    @Override
    public WorkflowType getWorkflowType() {
        return EXPORT_GENERATED_SQL;
    }
}
