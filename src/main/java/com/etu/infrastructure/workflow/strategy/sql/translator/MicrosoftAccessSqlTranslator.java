package com.etu.infrastructure.workflow.strategy.sql.translator;

import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;
import com.etu.infrastructure.state.dto.runtime.sql.DBMS;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTable;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationTableColumn;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType.*;

@Component
public class MicrosoftAccessSqlTranslator extends AbstractSqlTranslator {

    public static final String CALL_TEMPLATE = "    cmd.CommandText = \"%s\"\n" +
            "    cmd.Execute\n";
    public static final String TEMPLATE = "Option Compare Database\n" +
            "Option Explicit\n" +
            "Sub Main()\n" +
            "    Dim cmd As New ADODB.Command\n" +
            "    \n" +
            "    cmd.ActiveConnection = CurrentProject.Connection\n" +
            "%s" +
            "End Sub\n" +
            "\n" +
            "\n";

    @Override
    public List<DBMS> getSupportedDbms() {
        return Collections.singletonList(DBMS.MicrosoftAccess);
    }

    @Override
    protected String decorateResult(List<String> sqlStatements) {
        String decoratedSqlStatements = sqlStatements.stream()
                .map(sql -> String.format(CALL_TEMPLATE, sql))
                .collect(Collectors.joining());

        return String.format(TEMPLATE, decoratedSqlStatements);
    }

    @Override
    protected void translateSchemaStatements(SqlGenerationState source, List<String> statements) {
        // no need
    }

    @Override
    protected String decorate(String text) {
        return String.format("[%s]", text);
    }

    @Override
    protected String getFullTableName(SqlGenerationState state, SqlGenerationTable table) {
        return decorate(table.getName());
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
    protected String translateModifiers(SqlGenerationTableColumn column) {
        return column.isPrimary() ? "NOT NULL" : "NULL";
    }

    @Override
    protected String getDelimiter() {
        return ";";
    }
}
