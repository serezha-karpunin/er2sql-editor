package com.etu.infrastructure.workflow.strategy.sql.dto;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;

import java.util.ArrayList;
import java.util.List;

public class SqlGenerationTable {
    private String id;
    private String name;
    private List<SqlGenerationTableColumn> columns;
    private List<SqlGenerationTableIndex> indexes;
    private List<SqlGenerationTableForeignKey> foreignKeys;

    private RModelRelation sourceRelation;

    public SqlGenerationTable() {
        columns = new ArrayList<>();
        indexes = new ArrayList<>();
        foreignKeys = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<SqlGenerationTableIndex> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<SqlGenerationTableIndex> indexes) {
        this.indexes = indexes;
    }

    public List<SqlGenerationTableForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<SqlGenerationTableForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public RModelRelation getSourceRelation() {
        return sourceRelation;
    }

    public void setSourceRelation(RModelRelation sourceRelation) {
        this.sourceRelation = sourceRelation;
    }
}
