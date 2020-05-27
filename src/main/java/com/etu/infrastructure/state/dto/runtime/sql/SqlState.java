package com.etu.infrastructure.state.dto.runtime.sql;

import java.util.HashMap;
import java.util.Map;

public class SqlState {
    private Map<DBMS, String> sqlMap;

    public SqlState() {
        this.sqlMap = new HashMap<>();
    }

    public Map<DBMS, String> getSqlMap() {
        return sqlMap;
    }

    public void setSqlMap(Map<DBMS, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
}
