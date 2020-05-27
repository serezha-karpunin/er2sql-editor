package com.etu.infrastructure.workflow.strategy.sql.translator;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;
import com.etu.infrastructure.state.dto.runtime.sql.DBMS;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTable;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTableColumn;
import org.jooq.DataType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType.*;

@Component
public class DefaultSqlTranslator extends AbstractSqlTranslator {
    private Map<RModelRelationAttributeDataType, DataType<?>> dataTypeMapping;

    @Override
    public List<DBMS> getSupportedDbms() {
        return Arrays.asList(DBMS.MySQL, DBMS.PostgreSQL);
    }

    @Override
    protected void translateSchemaStatements(SqlGenerationState source, List<String> statements) {
        String schemaName = decorate(source.getSchemaName());
        String statement = String.format(CREATE_SCHEMA_STATEMENT_TEMPLATE, schemaName) + getDelimiter();
        statements.add(statement);
    }

    @Override
    protected String decorate(String text) {
        return String.format("`%s`", text);
    }

    @Override
    protected String decorateResult(List<String> sqlStatements) {
        return String.join("\n", sqlStatements);
    }

    @Override
    protected String getFullTableName(SqlGenerationState state, SqlGenerationTable table) {
        return String.format("%s.%s", decorate(state.getSchemaName()), decorate(table.getName()));
    }

    @Override
    protected String translateModifiers(SqlGenerationTableColumn column) {
        return column.isPrimary() ? "NOT NULL" : "NULL";
    }

    @Override
    protected String translateDataType(RModelRelationAttributeDataType dataType) {
        Map<RModelRelationAttributeDataType, String> map = new HashMap<>();
        map.put(BIT, "BIT");
        map.put(TINYINT, "TINYINT");
        map.put(SMALLINT, "SMALLINT");
        map.put(INT, "INT");
        map.put(REAL, "REAL");
        map.put(FLOAT, "FLOAT");
        map.put(DECIMAL, "DECIMAL");
        map.put(DATE, "DATE");
        map.put(TIME, "TIME");
        map.put(DATETIME, "DATETIME");
        map.put(CHAR, "CHAR(255)");
        map.put(OLE, "BLOB");
        return map.get(dataType);
    }

    @Override
    protected String getDelimiter() {
        return ";";
    }
}
