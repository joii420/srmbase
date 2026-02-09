package com.step.jdbc.runtime.flink.dictionary.search;


import com.step.jdbc.runtime.flink.dictionary.database.ProcessDatabase;
import com.step.jdbc.runtime.flink.dictionary.database.tdengine.TDengineDatabase;
import com.step.jdbc.runtime.flink.dictionary.datasource.SelectDatasource;
import com.step.jdbc.runtime.flink.dictionary.utils.TDengineUtils;
import com.step.jdbc.runtime.flink.session.DBSession;
import com.step.jdbc.runtime.flink.session.DBSessionConnection;
import com.step.jdbc.runtime.flink.session.RelationDBSession;
import com.step.jdbc.runtime.flink.session.TDEngineDBSession;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TDengineSearch implements ProcessSearchSupport, Serializable {
//    private final Object lock = new Object();

    @Override
    public List<Map<String, Object>> searchData(SelectDatasource processDatasource, ProcessDatabase processDatabase, Map<String, String> datas) {
        String name = processDatasource.getGroupId();
        TDengineDatabase tDengineDatabase = (TDengineDatabase) processDatabase;
        Long databaseId = tDengineDatabase.getId();
        DBSession dbSession = DBSessionConnection.getDBSession(name, databaseId);
        if (dbSession == null) {
            synchronized (this){
                dbSession = DBSessionConnection.getDBSession(name, databaseId);
                if(dbSession == null){
                    TDEngineDBSession dbSessions = new TDEngineDBSession(tDengineDatabase, 16);
                    DBSessionConnection.addDBSession(name, databaseId, dbSessions);
                }

            }

        }
        TDEngineDBSession tDEngineDBSession = (TDEngineDBSession) DBSessionConnection.getDBSession(name, databaseId);
        TDengineUtils tdSession = tDEngineDBSession.getTDSession();
        //sql
        String command = processDatasource.getCommand();
        //替换 sql
        String replacePlaceholders = replacePlaceholders(command, datas);

        try {
            return tdSession.query( replacePlaceholders);
//            return tdSession.query(processDatasource.getResultFields(), replacePlaceholders);
        } finally {
            tDEngineDBSession.relase(tdSession); //释放资源
        }

    }

    @Override
    public void execute(SelectDatasource processDatasource, ProcessDatabase processDatabase, Map<String, String> datas) {
        String name = processDatasource.getGroupId();
        TDengineDatabase tDengineDatabase = (TDengineDatabase) processDatabase;
        Long databaseId = tDengineDatabase.getId();
        DBSession dbSession = DBSessionConnection.getDBSession(name, databaseId);
        if (dbSession == null) {
            synchronized (this){
                dbSession = DBSessionConnection.getDBSession(name, databaseId);
                if(dbSession == null){
                    TDEngineDBSession dbSessions = new TDEngineDBSession(tDengineDatabase, 16);
                    DBSessionConnection.addDBSession(name, databaseId, dbSessions);
                }

            }

        }
        TDEngineDBSession tDEngineDBSession = (TDEngineDBSession) DBSessionConnection.getDBSession(name, databaseId);
        TDengineUtils tdSession = tDEngineDBSession.getTDSession();
        //sql
        String command = processDatasource.getCommand();
        //替换 sql
        String replacePlaceholders = replacePlaceholders(command, datas);

        try {
             tdSession.execute( replacePlaceholders);
//            return tdSession.query(processDatasource.getResultFields(), replacePlaceholders);
        } finally {
            tDEngineDBSession.relase(tdSession); //释放资源
        }
    }
}
