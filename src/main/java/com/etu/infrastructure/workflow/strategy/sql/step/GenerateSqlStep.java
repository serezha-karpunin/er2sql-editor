package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;

public interface GenerateSqlStep {
    void process(RModelState source, SqlGenerationState target);
}
