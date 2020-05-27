package com.etu.infrastructure.workflow.strategy.sql.translator;

import com.etu.infrastructure.state.dto.runtime.sql.DBMS;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;

import java.util.List;

public interface SqlTranslator {
    List<DBMS> getSupportedDbms();

    String translate(SqlGenerationState sqlState);
}
