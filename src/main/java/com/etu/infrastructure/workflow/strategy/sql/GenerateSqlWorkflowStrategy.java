package com.etu.infrastructure.workflow.strategy.sql;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.state.dto.runtime.sql.SqlState;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.infrastructure.workflow.strategy.SimpleWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import com.etu.infrastructure.workflow.strategy.sql.step.*;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.SQL_GENERATED;
import static com.etu.infrastructure.workflow.service.WorkflowType.SQL_GENERATION;

@Component
public class GenerateSqlWorkflowStrategy implements SimpleWorkflowStrategy {

    @Autowired
    private ProjectStateService projectStateService;
    @Autowired
    private EventFacade eventFacade;

    @Autowired
    private SelectSchemaNameStep selectSchemaNameStep;
    @Autowired
    private SortRelationsStep sortTablesStep;
    @Autowired
    private CreateTablesStep createTablesStep;
    @Autowired
    private CreateReferencesStep createReferencesStep;
    @Autowired
    private IntroduceNewKeysStep introduceNewKeysStep;
    @Autowired
    private CreateForeignKeysStep createForeignKeysStep;
    @Autowired
    private SqlStateFactory sqlStateFactory;

    @Override
    public void execute() {
        RModelState rmState = projectStateService.getProjectState().getRModelState();

        SqlState sqlState = generateSql(rmState);

        projectStateService.getProjectState().setSqlState(sqlState);
        eventFacade.fireEvent(new Event(SQL_GENERATED));
    }

    private SqlState generateSql(RModelState rmState) {
        SqlGenerationState sqlGenerationState = new SqlGenerationState();

        // 1.
        selectSchemaNameStep.process(rmState, sqlGenerationState);
        // 2.
        sortTablesStep.process(rmState, sqlGenerationState);
        // 3.
        createTablesStep.process(rmState, sqlGenerationState);
        // 4.
        createReferencesStep.process(rmState, sqlGenerationState);
        // 5.
        introduceNewKeysStep.process(rmState, sqlGenerationState);
        // 6.
        createForeignKeysStep.process(rmState, sqlGenerationState);

        return createSqlState(sqlGenerationState);
    }

    private SqlState createSqlState(SqlGenerationState sqlGenerationState) {
        return sqlStateFactory.create(sqlGenerationState);
    }

    @Override
    public WorkflowType getWorkflowType() {
        return SQL_GENERATION;
    }
}
