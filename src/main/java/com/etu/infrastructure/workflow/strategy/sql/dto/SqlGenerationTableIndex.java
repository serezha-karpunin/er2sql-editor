package com.etu.infrastructure.workflow.strategy.sql.dto;

import java.util.List;

public class SqlGenerationTableIndex {
    private String name;
    private List<SqlGenerationTableColumn> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SqlGenerationTableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<SqlGenerationTableColumn> columns) {
        this.columns = columns;
    }
}
