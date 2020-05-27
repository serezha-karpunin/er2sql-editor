package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTable;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTableColumn;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CreateTablesStep implements GenerateSqlStep {

    @Override
    public void process(RModelState source, SqlGenerationState target) {
        target.getSourceRelations().forEach(relation -> processTable(relation, source, target));
    }

    private void processTable(RModelRelation relation, RModelState source, SqlGenerationState target) {
        SqlGenerationTable table = new SqlGenerationTable();
        table.setId(relation.getId());
        table.setName(relation.getName());
        table.setColumns(createColumns(relation));
        table.setSourceRelation(relation);

        target.getTables().add(table);
    }

    private List<SqlGenerationTableColumn> createColumns(RModelRelation relation) {
        return relation.getAttributes().stream()
                .map(this::createColumn)
                .collect(toList());
    }

    private SqlGenerationTableColumn createColumn(RModelRelationAttribute attribute) {
        SqlGenerationTableColumn sqlGenerationTableAttribute = new SqlGenerationTableColumn();
        sqlGenerationTableAttribute.setPrimary(attribute.isKey());
        sqlGenerationTableAttribute.setName(attribute.getName());
        sqlGenerationTableAttribute.setDataType(attribute.getType());
        sqlGenerationTableAttribute.setSourceAttribute(attribute);

        return sqlGenerationTableAttribute;
    }
}
