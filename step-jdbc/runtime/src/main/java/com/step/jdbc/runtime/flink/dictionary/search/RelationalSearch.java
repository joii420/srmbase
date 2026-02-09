package com.step.jdbc.runtime.flink.dictionary.search;

import com.step.jdbc.runtime.flink.dictionary.database.ProcessDatabase;
import com.step.jdbc.runtime.flink.dictionary.database.RelationalDatabase;
import com.step.jdbc.runtime.flink.dictionary.datasource.SelectDatasource;
import com.step.jdbc.runtime.flink.session.DBSession;
import com.step.jdbc.runtime.flink.session.DBSessionConnection;
import com.step.jdbc.runtime.flink.session.RelationDBSession;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.session.JdbcSession;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RelationalSearch implements ProcessSearchSupport, Serializable {

//    private  Object lock = new Object();

    @Override
    public List<Map<String, Object>> searchData(SelectDatasource processDatasource, ProcessDatabase processDatabase, Map<String, String> datas) {
        String replacePlaceholders = replacePlaceholders(processDatasource.getCommand(), datas);
        RelationalDatabase relationalDatabase = (RelationalDatabase) processDatabase;
        RelationDBSession session = getSession(processDatasource, relationalDatabase);
        JdbcSession jdbcSession = session.getJdbcSession();
        try {
            return jdbcSession.queryUnClose(replacePlaceholders);
        } finally {
            session.relase(jdbcSession);
        }
    }


    @Override
    public void execute(SelectDatasource processDatasource, ProcessDatabase processDatabase, Map<String, String> datas) {
        String replacePlaceholders = replacePlaceholders(processDatasource.getCommand(), datas);
        RelationalDatabase relationalDatabase = (RelationalDatabase) processDatabase;
        RelationDBSession session = getSession(processDatasource, relationalDatabase);
        JdbcSession jdbcSession = session.getJdbcSession();
        try {
             jdbcSession.executeUnClose(replacePlaceholders);
        } finally {
            session.relase(jdbcSession);
        }
    }

    private RelationDBSession getSession(SelectDatasource processDatasource, RelationalDatabase relationalDatabase) {
        String name = processDatasource.getGroupId();
        JdbcParam jdbcParam = relationalDatabase.tranJdbcParm();
        Long databaseId = relationalDatabase.getId();
        DBSession dbSession = DBSessionConnection.getDBSession(name, databaseId);
        if (dbSession == null) {
            synchronized (this) {
                dbSession = DBSessionConnection.getDBSession(name, databaseId);
                if (dbSession == null) {
                    RelationDBSession dbSessions = new RelationDBSession(jdbcParam, 1);
//            relationalDatabase.setOpen(true);
                    DBSessionConnection.addDBSession(name, databaseId, dbSessions);
                }

            }

        }
        return (RelationDBSession) DBSessionConnection.getDBSession(name, databaseId);
    }

}
