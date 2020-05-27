package com.etu.infrastructure.workflow.strategy.sql.translator;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;
import com.etu.infrastructure.workflow.strategy.sql.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSqlTranslator implements SqlTranslator {

    protected static final String CREATE_SCHEMA_STATEMENT_TEMPLATE = "CREATE SCHEMA %s";
    protected static final String CREATE_TABLE_STATEMENT_TEMPLATE = "CREATE TABLE %s (%s, %s)";
    protected static final String CREATE_UNIQUE_INDEX_STATEMENT_TEMPLATE = "CREATE UNIQUE INDEX %s ON %s (%s)";

    protected static final String COLUMN_DEFINITION_TEMPLATE = "%s %s %s";

    protected static final String CONSTRAINT_PK_TEMPLATE = "CONSTRAINT %s PRIMARY KEY (%s)";
    protected static final String CONSTRAINT_UNIQUE_TEMPLATE = "CONSTRAINT %s UNIQUE (%s)";
    protected static final String CONSTRAINT_FK_TEMPLATE = "CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s (%s)";

    @Override
    public String translate(SqlGenerationState source) {
        List<String> sqlStatements = new ArrayList<>();

        translateSchemaStatements(source, sqlStatements);
        translateTableStatements(source, sqlStatements);

        return decorateResult(sqlStatements);
    }

    protected abstract String decorateResult(List<String> sqlStatements);

    protected abstract void translateSchemaStatements(SqlGenerationState source, List<String> statements);

    private void translateTableStatements(SqlGenerationState source, List<String> sqlStatements) {
        source.getTables()
                .forEach(table -> translateTableStatements(source, table, sqlStatements));
    }

    protected void translateTableStatements(SqlGenerationState state, SqlGenerationTable table, List<String> sqlStatements) {
        String createTableStatement = translateCreateTableStatement(state, table);
        sqlStatements.add(createTableStatement);

        List<String> createIndexStatements = translateCreateIndexStatements(state, table);
        sqlStatements.addAll(createIndexStatements);
    }

    private String translateCreateTableStatement(SqlGenerationState state, SqlGenerationTable table) {
        String tableName = getFullTableName(state, table);
        String columns = translateColumns(table);
        String constraints = translateConstraints(table);

        return String.format(CREATE_TABLE_STATEMENT_TEMPLATE, tableName, columns, constraints) + getDelimiter();
    }

    protected abstract String getFullTableName(SqlGenerationState state, SqlGenerationTable table);

    private String translateColumns(SqlGenerationTable table) {
        return table.getColumns().stream()
                .map(this::translateColumn)
                .collect(Collectors.joining(", "));
    }

    private String translateColumn(SqlGenerationTableColumn column) {
        String columnName = decorate(column.getName());
        String dataType = translateDataType(column.getDataType());
        String modifiers = translateModifiers(column);

        return String.format(COLUMN_DEFINITION_TEMPLATE, columnName, dataType, modifiers);
    }

    protected abstract String decorate(String text);

    protected abstract String translateDataType(RModelRelationAttributeDataType dataType);

    protected abstract String translateModifiers(SqlGenerationTableColumn column);

    private String translateConstraints(SqlGenerationTable table) {
        List<String> constraints = new ArrayList<>();

        if (hasPrimaryKeys(table)) {
            String pkConstraint = translatePkConstraint(table);
            constraints.add(pkConstraint);

            String uniqueConstraint = translateUniqueConstraint(table);
            constraints.add(uniqueConstraint);
        }

        if (hasForeignKeys(table)) {
            List<String> fkConstraints = translateFkConstraint(table);
            constraints.addAll(fkConstraints);
        }

        return String.join(", ", constraints);
    }

    private boolean hasPrimaryKeys(SqlGenerationTable table) {
        return table.getColumns().stream()
                .anyMatch(SqlGenerationTableColumn::isPrimary);
    }

    private List<String> getPrimaryKeyColumns(SqlGenerationTable table) {
        return table.getColumns().stream()
                .filter(SqlGenerationTableColumn::isPrimary)
                .map(SqlGenerationTableColumn::getName)
                .map(this::decorate)
                .collect(Collectors.toList());
    }

    private String translatePkConstraint(SqlGenerationTable table) {
        String constraintName = decorate("PK");
        String columnNames = String.join(", ", getPrimaryKeyColumns(table));

        return String.format(CONSTRAINT_PK_TEMPLATE, constraintName, columnNames);
    }

    private String translateUniqueConstraint(SqlGenerationTable table) {
        String constraintName = decorate("UNQ");
        String columnNames = String.join(", ", getPrimaryKeyColumns(table));

        return String.format(CONSTRAINT_UNIQUE_TEMPLATE, constraintName, columnNames);
    }

    private boolean hasForeignKeys(SqlGenerationTable table) {
        return !table.getForeignKeys().isEmpty();
    }

    private List<String> translateFkConstraint(SqlGenerationTable table) {
        return table.getForeignKeys().stream()
                .map(this::translateFkConstraint)
                .collect(Collectors.toList());
    }

    private String translateFkConstraint(SqlGenerationTableForeignKey foreignKey) {
        String constraintName = decorate(foreignKey.getName());

        String columnNames = foreignKey.getLinkedAttributes().keySet().stream()
                .map(SqlGenerationTableColumn::getName)
                .map(this::decorate)
                .collect(Collectors.joining(", "));

        String linkedTableName = decorate(foreignKey.getLinkedTable().getName());

        String linkedColumnNames = foreignKey.getLinkedAttributes().values().stream()
                .map(SqlGenerationTableColumn::getName)
                .map(this::decorate)
                .collect(Collectors.joining(", "));

        return String.format(CONSTRAINT_FK_TEMPLATE, constraintName, columnNames, linkedTableName, linkedColumnNames);
    }

    private List<String> translateCreateIndexStatements(SqlGenerationState state, SqlGenerationTable table) {
        return table.getIndexes().stream()
                .map(index -> translateCreateIndexStatement(state, table, index))
                .collect(Collectors.toList());
    }

    private String translateCreateIndexStatement(SqlGenerationState state, SqlGenerationTable table, SqlGenerationTableIndex index) {
        String indexName = decorate(index.getName());
        String tableName = getFullTableName(state, table);

        String columnNames = index.getColumns().stream()
                .map(SqlGenerationTableColumn::getName)
                .map(this::decorate)
                .collect(Collectors.joining(", "));

        return String.format(CREATE_UNIQUE_INDEX_STATEMENT_TEMPLATE, indexName, tableName, columnNames) + getDelimiter();
    }

    protected abstract String getDelimiter();
}
