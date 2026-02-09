package com.step.jdbc.runtime.txedit.provider.test.txedit.factory;

import com.step.api.runtime.core.CacheAPI;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.txedit.exception.JdbcError;
import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.logger.LOGGER;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Sun
 * @date : 2023/1/28  10:05
 */
@Singleton
public class JdbcFactory {

    /**
     * 根据用户信息获取用户的事务锁定参数
     */
    //@TODO 需要定时清除
    private volatile Map<String, LockParam> lockParamMap = new ConcurrentHashMap<>();

    /**
     * 初始化连接信息
     *
     * @param jdbcParam jdbc连接参数
     * @return
     */
    public Connection initConnection(JdbcParam jdbcParam) {
        try {
            LOGGER.info("事务建立连接: url: %s", jdbcParam.getUrl());
            //连接成功
            return DriverManager.getConnection(jdbcParam.getUrl(), jdbcParam.getUsername(), jdbcParam.getPassword());
        } catch (SQLException e) {
            LOGGER.error(JdbcError.CON_003.getMessage(), e);
            throw new BaseException(e);
        }
    }

    public LockParam getLockParam(String tranKey) {
        return lockParamMap.get(tranKey);
    }

    public void saveLockParam(String tranKey, LockParam lockParam) {
        lockParamMap.put(tranKey, lockParam);
    }

    public LockParam removeLockParam(String tranKey) {
        return lockParamMap.remove(tranKey);
    }
}
