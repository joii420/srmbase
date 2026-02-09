package com.step.jdbc.runtime.flink.session;

import com.step.jdbc.runtime.flink.dictionary.database.tdengine.TDengineDatabase;
import com.step.jdbc.runtime.flink.dictionary.utils.TDengineUtils;

public class TDEngineDBSession extends DBSession{
    private boolean isOpen;

    private TDEngineDatabaseThreadPool tdEngineDatabaseThreadPool;
    @Override
    public void close() {
        if(tdEngineDatabaseThreadPool!=null){
            tdEngineDatabaseThreadPool.shutdown();
        }
    }
    public TDengineUtils getTDSession() {
        return tdEngineDatabaseThreadPool.getConnection();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
    public TDEngineDBSession(TDengineDatabase tDengineDatabase, int size){
        tdEngineDatabaseThreadPool =  new TDEngineDatabaseThreadPool(tDengineDatabase,size);
    }

    public void relase(TDengineUtils tDengineUtils){
        this.tdEngineDatabaseThreadPool.releaseConnection(tDengineUtils);
    }
}
