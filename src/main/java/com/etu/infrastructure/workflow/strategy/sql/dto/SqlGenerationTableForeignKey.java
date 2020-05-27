package com.etu.infrastructure.workflow.strategy.sql.dto;

import java.util.Map;

public class SqlGenerationTableForeignKey {
    private String name;
    private SqlGenerationTable linkedTable;
    private Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedAttributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SqlGenerationTable getLinkedTable() {
        return linkedTable;
    }

    public void setLinkedTable(SqlGenerationTable linkedTable) {
        this.linkedTable = linkedTable;
    }

    public Map<SqlGenerationTableColumn, SqlGenerationTableColumn> getLinkedAttributes() {
        return linkedAttributes;
    }

    public void setLinkedAttributes(Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedAttributes) {
        this.linkedAttributes = linkedAttributes;
    }
}
