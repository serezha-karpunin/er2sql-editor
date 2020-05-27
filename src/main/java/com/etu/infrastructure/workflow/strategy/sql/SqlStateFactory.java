package com.etu.infrastructure.workflow.strategy.sql;

import com.etu.infrastructure.state.dto.runtime.sql.DBMS;
import com.etu.infrastructure.state.dto.runtime.sql.SqlState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.translator.SqlTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class SqlStateFactory {

    @Autowired
    private List<SqlTranslator> translators;

    public SqlState create(SqlGenerationState source) {

        Map<DBMS, String> sqlStatements = new HashMap<>();
        Stream.of(DBMS.values())
                .forEach(dbms -> sqlStatements.put(dbms, getSqlStatementsFor(dbms, source)));

        SqlState target = new SqlState();
        target.setSqlMap(sqlStatements);

        return target;
    }

    private String getSqlStatementsFor(DBMS dbms, SqlGenerationState source) {
        return getSqlTranslator(dbms).translate(source);
    }

    private SqlTranslator getSqlTranslator(DBMS dbms) {
        return translators.stream()
                .filter(translator -> translator.getSupportedDbms().contains(dbms))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
