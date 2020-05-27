package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationReference;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTableColumn;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTableForeignKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CreateForeignKeysStep implements GenerateSqlStep {
    @Override
    public void process(RModelState source, SqlGenerationState target) {
        target.getReferences().forEach(reference -> createForeignKey(reference, source, target));
    }

    private void createForeignKey(SqlGenerationReference reference, RModelState source, SqlGenerationState target) {
        Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedColumnsMap = new LinkedHashMap<>();
        reference.getLinkedAttributes().forEach((k, v) -> linkedColumnsMap.put(v, k));

        SqlGenerationTableForeignKey foreignKey = new SqlGenerationTableForeignKey();
        foreignKey.setName(String.format("FK_%s_%s", reference.getTableTo().getName(), reference.getTableFrom().getName()));
        foreignKey.setLinkedTable(reference.getTableFrom());
        foreignKey.setLinkedAttributes(linkedColumnsMap);

        reference.getTableTo().getForeignKeys().add(foreignKey);
    }
}
