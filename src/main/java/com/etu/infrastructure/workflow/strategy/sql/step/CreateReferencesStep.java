package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.state.dto.runtime.rm.*;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationReference;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTable;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTableColumn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CreateReferencesStep implements GenerateSqlStep {
    @Override
    public void process(RModelState source, SqlGenerationState target) {
        source.getLinks().forEach(link -> createReference(link, source, target));
    }

    private void createReference(RModelLink link, RModelState source, SqlGenerationState target) {
        RModelLinkSide linkSideFrom = link.getLinkSideFrom();
        SqlGenerationTable tableFrom = getTableFor(target, linkSideFrom);

        RModelLinkSide linkSideTo = link.getLinkSideTo();
        SqlGenerationTable tableTo = getTableFor(target, linkSideTo);

        Map<SqlGenerationTableColumn, SqlGenerationTableColumn> linkedAttributes = new LinkedHashMap<>();

        link.getLinkedAttributesMap().forEach(
                (attributeFrom, attributeTo) -> {
                    SqlGenerationTableColumn columnFrom = getColumnFor(tableFrom, attributeFrom);
                    SqlGenerationTableColumn columnTo = getColumnFor(tableTo, attributeTo);

                    linkedAttributes.put(columnFrom, columnTo);
                }
        );

        SqlGenerationReference reference = new SqlGenerationReference();
        reference.setTableFrom(tableFrom);
        reference.setTableTo(tableTo);
        reference.setLinkedAttributes(linkedAttributes);

        target.getReferences().add(reference);
    }

    private SqlGenerationTableColumn getColumnFor(SqlGenerationTable table, RModelRelationAttribute attribute) {
        return table.getColumns().stream()
                .filter(column -> column.getSourceAttribute() == attribute)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private SqlGenerationTable getTableFor(SqlGenerationState target, RModelLinkSide linkSide) {
        RModelRelation relation = linkSide.getRelation();

        return target.getTables().stream()
                .filter(table -> table.getSourceRelation() == relation)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
