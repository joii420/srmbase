package com.step.jdbc.runtime.txedit.provider.test.txedit.provider;

import com.step.api.runtime.core.CacheAPI;
import com.step.api.runtime.exception.ResInfo;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.api.TransactionEditAPI;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.txedit.config.TransactionConfig;
import com.step.jdbc.runtime.txedit.exception.JdbcError;
import com.step.jdbc.runtime.txedit.exception.OperationErrorCode;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.jdbc.runtime.txedit.model.TransactionSql;
import com.step.jdbc.runtime.txedit.model.TxR;
import com.step.jdbc.runtime.txedit.model.UserTx;
import com.step.jdbc.runtime.txedit.provider.test.txedit.factory.JdbcFactory;
import com.step.logger.LOGGER;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author : Sun
 * @date : 2023/1/5  13:17
 */

@Singleton
public class JdbcEditor implements EditorAPI {

    public static final Logger log = LoggerFactory.getLogger(JdbcEditor.class);
    @Inject
    CacheAPI cacheAPI;

    public JdbcEditor() {

    }

    @Inject
    JdbcFactory jdbcFactory;

    @Override
    public TxR beginEdit(LockParam lockParam) {
        String tranKey = lockParam.getTranKey();
        TxR txR = new TxR(tranKey);
        LOGGER.info(log, tranKey + ": 开启编辑");
        //开启编辑状态第一步获取数据锁成功.进行第二步, 设置数据库行锁
        //1.初始化数据源jdbc连接
        //2.使用jdbc执行行锁sql  如果失败则移除则异常退出
        LockParam cache = jdbcFactory.getLockParam(tranKey);
        String lockSql = lockParam.getHandleLockSql();
        if (cache != null) {
            LOGGER.info(log, "获取到缓存的JDBC连接 : [ %s ]", tranKey);
            lockParam = cache;
            UserTx userTx = lockParam.getUserTx();
            if (userTx == null) {
                return new TxR(OperationErrorCode.INVALID_JDBC_SOURCE);
            }
            Statement stat = userTx.getStat();
            try {
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException e) {
                LOGGER.error(log, "关闭statement 失败");
            }
            LOGGER.info(log, "数据锁定SQL : %s", lockSql);
            Statement statement;
            try {
                statement = setRowLock(lockParam.getUserTx().getConnection(), lockSql);
            } catch (BaseException e) {
                return new TxR(e.getMessage());
            }
            userTx.setStat(statement);
            lockParam.setUserTx(userTx);
        } else {
            LOGGER.info(log, "开启新的JDBC连接 : [ %s ]", tranKey);
            JdbcParam jdbcParam = lockParam.getJdbcParam();
//            checkJdbcParam(jdbcParam);
            if (jdbcParam == null || StringUtil.isEmpty(jdbcParam.getUrl(), jdbcParam.getUsername(), jdbcParam.getPassword())) {
                return new TxR(JdbcError.CON_002);
            }
//            //数据库类型
//            String driverType = jdbcParam.getDriverType();
//            //获取驱动信息
//            jdbcParam.setDriver(DriverType.getDriver(driverType));
            Connection connection = jdbcFactory.initConnection(jdbcParam);
            //执行数据行锁操作
            Statement statement;
            try {
                statement = setRowLock(connection, lockSql);
            } catch (BaseException e) {
                return new TxR(e.getMessage());
            }
            //锁定成功,保存事务状态到缓存数据中
            lockParam.setUserTx(new UserTx(connection, statement));
        }
        //  如果成功则保存到本地缓存 事务状态对象缓存到服务器本地
        jdbcFactory.saveLockParam(tranKey, lockParam);
        LOGGER.info(log, "开启编辑成功 : ID[ %s ]", tranKey);
        return txR;
    }

    /**
     * 遍历执行sql 进行数据库上行锁
     *
     * @param connection 数据库连接
     * @param lockSql    for update sqlLists
     */
    private Statement setRowLock(Connection connection, String lockSql) {
        //声明结果
        boolean result;
        Statement stat;
        try {
            //创建声明
            stat = connection.createStatement();
            //1.开启事务
            connection.setAutoCommit(false);
            result = executeSql(lockSql, stat);
        } catch (Exception e) {
            LOGGER.error(e, "数据库数据锁定失败");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.error(e, "关闭事务失败");
            }
            //锁定失败数据回滚  开启编辑失败
            throw new BaseException(e);
        }
        if (!result) {
            //锁定失败数据回滚  开启编辑失败
            throw new BaseException(OperationErrorCode.DATA_LOCKED.getMessage()).record();
        }
        return stat;
    }

    /**
     * 执行sql
     *
     * @param executeSql 执行的sql
     * @param stat       执行器
     * @return 执行结果
     * @throws SQLException sql异常
     */
    private boolean executeSql(String executeSql, Statement stat) throws SQLException {
        LOGGER.info("%s%s%s%n", "\033[34;4m", executeSql, "\033[0m");
        return stat.execute(executeSql);
    }

    /**
     * 执行sql
     *
     * @param executeSql 执行的sql
     * @param stat       执行器
     * @return 执行结果
     * @throws SQLException sql异常
     */
    private ResultSet executeQuerySql(String executeSql, Statement stat) throws SQLException {
        LOGGER.info("%s%s%s%n", "\033[34;4m", executeSql, "\033[0m");
        return stat.executeQuery(executeSql);
    }

    /**
     * 解锁之前的事务
     *
     * @param lock 锁定信息
     */
    @Override
    public void unlock(boolean isCommit, LockParam lock) {
        //todo 根据参数找到指定的服务器处理
        String tranKey = lock.getTranKey();
        //移除本地缓存的事务参数
        LockParam lockParam = jdbcFactory.removeLockParam(tranKey);
        //提交之前的事务
        commitTransaction(isCommit, lockParam);
    }

    @Override
    public TxR commitTransaction(boolean isCommit, LockParam lockParam) {
        TxR txR = new TxR(lockParam.getTranKey());
        UserTx userTx = lockParam.getUserTx();
        if (userTx == null) {
            txR.setResult(OperationErrorCode.INVALID_JDBC_SOURCE);
            return txR;
        }
        Connection connection = userTx.getConnection();
        Statement stat = userTx.getStat();
        if (connection != null) {
            try {
                if (isCommit) {
                    //提交事务
                    connection.commit();
                } else {
                    connection.rollback();
                }
            } catch (Exception e) {
                LOGGER.error(e, "提交事务失败");
                txR.setResult(OperationErrorCode.ERROR_EXIT);
                if (e instanceof SQLException sqlException) {
                    txR.getErrorInfo().formatSqlException(sqlException);
                }
                return txR;
            } finally {
                try {
                    if (stat != null) {
                        stat.close();
                    }
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error(e);
                }
            }
        }
        return txR;
    }

    @Override
    public TxR updateData(String lockSql, LockParam lockParam, TransactionSql transactionSql) {
        List<String> executeSqlList = transactionSql.getExecuteSqlList();
        String executeSql = TransactionEditAPI.concatSql(executeSqlList);
        LOGGER.info(log, "要执行的SQL: %s", executeSql);
        TxR txR = new TxR(lockParam.getTranKey());
        LOGGER.info(log, lockParam.getTranKey() + ": 更新数据");
        LOGGER.info(log, "TRANSACTION-EXECUTE-SQL :" + executeSql);
        //获取用户的事务状态
        UserTx userTx = lockParam.getUserTx();
        if (userTx == null) {
            LOGGER.error(log, "获取不到用户的信息: userTx");
            txR.setResult(OperationErrorCode.INVALID_EDIT);
            return txR;
        }
        //jdbc数据源绑定的事务管理器
        Connection connection = userTx.getConnection();
        Statement stat = userTx.getStat();
        if (stat == null || connection == null) {
            LOGGER.error(log, "获取不到连接信息: Connection || Statement is null");
            txR.setResult(OperationErrorCode.INVALID_EDIT);
            return txR;
        }
        TxR txR1 = verifySqlExecute(transactionSql, txR, stat);
        if (txR1 != null) {
            return txR1;
        }
        ResInfo resInfo = new ResInfo(OperationErrorCode.ERROR_EDIT_COMMIT_FAILED);
        try {
            //执行用户的更新操作行为
            executeSql(executeSql, stat);
            //提交事务
            connection.commit();
        } catch (Exception e) {
            if (e instanceof SQLException sqlException) {
                resInfo = new ResInfo(OperationErrorCode.ERROR_EXECUTE_SQL);
                resInfo.formatSqlException(sqlException);
            }
            txR.setErrorInfo(resInfo);
            LOGGER.error(log, e, "操作发生异常,数据回滚");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.error(log, ex, "数据回滚失败");
            }
        } finally {
            //清空事务状态信息
            try {
                stat.close();
            } catch (SQLException e) {
                LOGGER.error(log, e, "statement 关闭失败");
            }
            try {
                stat = connection.createStatement();
                userTx.setStat(stat);
            } catch (SQLException e) {
                LOGGER.error(log, e, "statement 创建失败");
            }
        }
        lockParam.setUserTx(userTx);
        try {
            //开启一个新的事务
            connection.setAutoCommit(false);
            //执行sql锁定数据行
            executeSql(lockSql, stat);
        } catch (Exception e) {
            LOGGER.error(log, e, "数据库数据锁定失败");
            //设置行锁失败,清除状态
            jdbcFactory.removeLockParam(lockParam.getTranKey());
            //清除缓存程序锁
            String programKey = TransactionConfig.getProgramKey(lockParam);
            cacheAPI.delete(programKey);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.error(log, ex, "数据回滚失败");
            }
            try {
                stat.close();
            } catch (SQLException ex) {
                LOGGER.error(log, ex, "statement 关闭失败");
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                LOGGER.error(log, ex, "connection 关闭失败");
            }
            resInfo = new ResInfo(OperationErrorCode.ERROR_EDIT);
            if (e instanceof SQLException sqlException) {
                resInfo = new ResInfo(OperationErrorCode.ERROR_EXECUTE_SQL);
                resInfo.formatSqlException(sqlException);
            }
            txR.setResult(OperationErrorCode.ERROR_EXECUTE_SQL);
            txR.setErrorInfo(resInfo);
        }
        //替换用户在服务器中缓存的事务状态
        jdbcFactory.saveLockParam(lockParam.getTranKey(), lockParam);
        return txR;
    }

    /**
     * 校验sql列表是否执行正常
     *
     * @param transactionSql sql列表
     * @param stat           执行器
     */
    @Nullable
    private TxR verifySqlExecute(TransactionSql transactionSql, TxR txR, Statement stat) {
        try {
            //如果需要校验数据是否已被更改
            List<String> verifySqlList = transactionSql.getVerifySqlList();
            if (CollectionUtil.isNotEmpty(verifySqlList)) {
                for (String verifySql : verifySqlList) {
                    ResultSet resultSet = executeQuerySql(verifySql, stat);
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        if (count < 1) {
                            txR.setResult(OperationErrorCode.ERROR_EDIT_DATA_EXPIRED);
                            return txR;
                        }
                    } else {
                        LOGGER.error("校验SQL异常: %s", verifySql);
                        txR.setResult(OperationErrorCode.ERROR_EXECUTE_SQL);
                        return txR;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(log, e, "校验修改数据时 执行发生错误: ");
            txR.setResult(OperationErrorCode.ERROR_EXECUTE_SQL);
            if (e instanceof SQLException sqlException) {
                txR.getErrorInfo().formatSqlException(sqlException);
            }
            return txR;
        }
        return null;
    }

    @Override
    public TxR updateDataNonCommit(LockParam lockParam, TransactionSql transactionSql) {
        List<String> executeSqlList = transactionSql.getExecuteSqlList();
        String executeSql = TransactionEditAPI.concatSql(executeSqlList);
        LOGGER.info(log, "updateDataNonCommit 要执行的SQL: %s", executeSql);
        TxR txR = new TxR(lockParam.getTranKey());
        LOGGER.info(log, lockParam.getTranKey() + ": 更新数据");
        LOGGER.info(log, "TRANSACTION-EXECUTE-SQL :" + executeSql);
        //获取用户的事务状态
        UserTx userTx = lockParam.getUserTx();
        if (userTx == null) {
            LOGGER.error(log, "获取不到用户的信息: userTx");
            txR.setResult(OperationErrorCode.INVALID_EDIT);
            return txR;
        }
        //jdbc数据源绑定的事务管理器
        Connection connection = userTx.getConnection();
        Statement stat = userTx.getStat();
        if (stat == null || connection == null) {
            LOGGER.error(log, "获取不到连接信息: Connection || Statement is null");
            txR.setResult(OperationErrorCode.INVALID_EDIT);
            return txR;
        }
        TxR txR1 = verifySqlExecute(transactionSql, txR, stat);
        if (txR1 != null) {
            return txR1;
        }
        ResInfo resInfo = new ResInfo(OperationErrorCode.ERROR_EDIT_COMMIT_FAILED);
        try {
            //执行用户的更新操作行为
            executeSql(executeSql, stat);
        } catch (Exception e) {
            if (e instanceof SQLException sqlException) {
                resInfo = new ResInfo(OperationErrorCode.ERROR_EXECUTE_SQL);
                resInfo.formatSqlException(sqlException);
            }
            txR.setErrorInfo(resInfo);
            LOGGER.error(log, e, "操作发生异常,数据回滚");
            //暂时不回滚
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                LOGGER.error(log, ex, "数据回滚失败");
//            }
        }
        return txR;
    }

    /**
     * 校验jdbc中的参数是否完整
     * 获取驱动信息
     *
     * @param jdbcParam 参数
     */
    private void checkJdbcParam(JdbcParam jdbcParam) {
        if (jdbcParam == null || StringUtil.isEmpty(jdbcParam.getUrl(), jdbcParam.getUsername(), jdbcParam.getPassword())) {
            throw new BaseException(JdbcError.CON_002.getMessage()).record();
        }
//        //数据库类型
//        String driverType = jdbcParam.getDriverType();
//        //获取驱动信息
//        jdbcParam.setDriver(DriverType.getDriver(driverType));
    }
}
