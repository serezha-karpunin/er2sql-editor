package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import javafx.scene.control.TextInputDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SelectSchemaNameStep implements GenerateSqlStep {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void process(RModelState source, SqlGenerationState target) {
        TextInputDialog dialog = applicationContext.getBean("schemaNameTextInputDialog", TextInputDialog.class);

        dialog.showAndWait()
                .ifPresent(target::setSchemaName);
    }
}
