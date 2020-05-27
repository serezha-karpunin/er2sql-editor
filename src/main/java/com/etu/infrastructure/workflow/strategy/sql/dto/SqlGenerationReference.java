package com.etu.infrastructure.workflow.strategy.sql.dto;

import java.util.Map;

public class SqlGenerationReference {
    private SqlGenerationTable tableFrom;
    private SqlGenerationTable tableTo;
    private Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedAttributes;

    public SqlGenerationTable getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(SqlGenerationTable tableFrom) {
        this.tableFrom = tableFrom;
    }

    public SqlGenerationTable getTableTo() {
        return tableTo;
    }

    public void setTableTo(SqlGenerationTable tableTo) {
        this.tableTo = tableTo;
    }

    public Map<SqlGenerationTableColumn, SqlGenerationTableColumn> getLinkedAttributes() {
        return linkedAttributes;
    }

    public void setLinkedAttributes(Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedAttributes) {
        this.linkedAttributes = linkedAttributes;
    }
}
