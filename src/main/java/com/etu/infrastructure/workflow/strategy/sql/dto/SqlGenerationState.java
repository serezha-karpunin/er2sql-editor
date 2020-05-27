package com.etu.infrastructure.workflow.strategy.sql.dto;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;

import java.util.ArrayList;
import java.util.List;

public class SqlGenerationState {
    private String schemaName;
    private List<RModelRelation> sourceRelations;
    private List<SqlGenerationTable> tables = new ArrayList<>();
    private List<SqlGenerationReference> references = new ArrayList<>();

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public List<RModelRelation> getSourceRelations() {
        return sourceRelations;
    }

    public void setSourceRelations(List<RModelRelation> sourceTables) {
        this.sourceRelations = sourceTables;
    }

    public List<SqlGenerationTable> getTables() {
        return tables;
    }

    public void setTables(List<SqlGenerationTable> tables) {
        this.tables = tables;
    }

    public List<SqlGenerationReference> getReferences() {
        return references;
    }

    public void setReferences(List<SqlGenerationReference> references) {
        this.references = references;
    }
}
