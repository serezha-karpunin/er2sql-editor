package com.etu.ui.sql.tab;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.state.dto.runtime.sql.DBMS;
import com.etu.infrastructure.state.dto.runtime.sql.SqlState;
import com.etu.ui.AbstractMediator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.SQL_GENERATED;

@Component
@Scope("prototype")
public class SqlTabMediator extends AbstractMediator {

    @Autowired
    private ProjectStateService projectStateService;
    @Autowired
    private EventFacade eventFacade;

    private StringProperty generatedSql = new SimpleStringProperty();

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(SQL_GENERATED, this::updateSql);
    }

    private void updateSql(Event event) {
        generatedSql.set(getSqlStatements(DBMS.MySQL));
    }

    public StringProperty generatedSqlProperty() {
        return generatedSql;
    }

    public void selectDbms(DBMS dbms) {
        generatedSql.set(getSqlStatements(dbms));
    }

    private String getSqlStatements(DBMS dbms) {
        SqlState sqlState = projectStateService.getProjectState().getSqlState();
        return sqlState.getSqlMap().get(dbms);
    }
}
