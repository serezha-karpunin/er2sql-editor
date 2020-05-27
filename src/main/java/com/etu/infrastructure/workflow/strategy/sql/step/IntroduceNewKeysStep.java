package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.sql.dto.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.etu.infrastructure.localization.LocalizationConstants.ALERT_SQL_CREATE_ID_CONTENT_TEXT_PROPERTY_NAME;
import static java.util.stream.Collectors.toList;

@Component
public class IntroduceNewKeysStep implements GenerateSqlStep {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LocalizationService localizationService;

    @Override
    public void process(RModelState source, SqlGenerationState target) {

        target.getTables().forEach(table -> process(table, source, target));
    }

    private void process(SqlGenerationTable table, RModelState source, SqlGenerationState target) {
        if (!hasPrimaryKeys(table)) {
            return;
        }

        Alert alert = applicationContext.getBean("shouldCreateId", Alert.class);
        alert.setContentText(getContentText(table));

        alert.showAndWait()
                .map(ButtonType::getButtonData)
                .filter(ButtonBar.ButtonData.YES::equals)
                .ifPresent(data -> createNewKey(table, source, target));
    }

    private boolean hasPrimaryKeys(SqlGenerationTable table) {
        return table.getColumns().stream()
                .anyMatch(SqlGenerationTableColumn::isPrimary);
    }

    private String getContentText(SqlGenerationTable table) {
        return String.format(localizationService.getLocalizedString(ALERT_SQL_CREATE_ID_CONTENT_TEXT_PROPERTY_NAME),
                table.getName(),
                getPrimaryColumnsString(table)
        );
    }

    private String getPrimaryColumnsString(SqlGenerationTable table) {
        return getPrimaryColumns(table).stream()
                .map(SqlGenerationTableColumn::getName)
                .collect(Collectors.joining(", "));
    }

    private List<SqlGenerationTableColumn> getPrimaryColumns(SqlGenerationTable table) {
        return table.getColumns().stream()
                .filter(SqlGenerationTableColumn::isPrimary)
                .collect(toList());
    }

    private void createNewKey(SqlGenerationTable table, RModelState source, SqlGenerationState target) {
        TextInputDialog dialog = applicationContext.getBean("idInputDialog", TextInputDialog.class);
        dialog.getEditor().setText("ID_" + table.getName());

        dialog.showAndWait()
                .ifPresent(idName -> createNewKey(idName, table, source, target));
    }

    private void createNewKey(String idName, SqlGenerationTable table, RModelState source, SqlGenerationState target) {
        List<SqlGenerationTableColumn> primaryColumns = getPrimaryColumns(table);

        makeNotPrimary(primaryColumns);
        SqlGenerationTableColumn idColumn = addIdColumn(idName, table);
        createIndex(primaryColumns, table);
        adjustReferences(idName, idColumn, table, target);
    }

    private void makeNotPrimary(List<SqlGenerationTableColumn> columns) {
        columns.forEach(column -> column.setPrimary(false));
    }

    private SqlGenerationTableColumn addIdColumn(String idName, SqlGenerationTable table) {
        SqlGenerationTableColumn idColumn = new SqlGenerationTableColumn();
        idColumn.setName(idName);
        idColumn.setPrimary(true);
        idColumn.setDataType(RModelRelationAttributeDataType.INT);
        table.getColumns().add(0, idColumn);

        return idColumn;
    }

    private void createIndex(List<SqlGenerationTableColumn> columns, SqlGenerationTable table) {
        SqlGenerationTableIndex index = new SqlGenerationTableIndex();
        index.setName("IDX_" + table.getName());
        index.setColumns(columns);

        table.getIndexes().add(index);
    }

    private void adjustReferences(String idName, SqlGenerationTableColumn idColumn, SqlGenerationTable table, SqlGenerationState sqlGenerationState) {
        getReferencesFor(table, sqlGenerationState).forEach(
                reference -> {
                    SqlGenerationTableColumn newColumn = new SqlGenerationTableColumn();
                    newColumn.setName(idName);
                    newColumn.setDataType(RModelRelationAttributeDataType.INT);

                    SqlGenerationTable linkedTable = reference.getTableTo();
                    Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedAttributesMap = reference.getLinkedAttributes();
                    Collection<SqlGenerationTableColumn> linkedAttributes = linkedAttributesMap.values();

                    if (linkedAttributes.stream().allMatch(SqlGenerationTableColumn::isPrimary)) {
                        newColumn.setPrimary(true);
                        linkedAttributes.forEach(column -> column.setPrimary(false));

                        linkedTable.getColumns().add(0, newColumn);
                        linkedTable.getColumns().removeAll(reference.getLinkedAttributes().values());
                        reference.getLinkedAttributes().clear();
                        reference.getLinkedAttributes().put(idColumn, newColumn);

                        adjustReferences(idName, newColumn, linkedTable, sqlGenerationState);
                    } else {
                        linkedTable.getColumns().add(0, newColumn);
                        linkedTable.getColumns().removeAll(reference.getLinkedAttributes().values());

                        reference.getLinkedAttributes().clear();
                        reference.getLinkedAttributes().put(idColumn, newColumn);
                    }

                }
        );
    }

    private List<SqlGenerationReference> getReferencesFor(SqlGenerationTable table, SqlGenerationState sqlGenerationState) {
        return sqlGenerationState.getReferences().stream()
                .filter(reference -> reference.getTableFrom() == table)
                .collect(Collectors.toList());
    }
}
