package com.step.jdbc.runtime.api;

import com.step.api.runtime.exception.ServerCode;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.option.TransactionOption;
import com.step.jdbc.runtime.txedit.model.TransactionSql;
import com.step.jdbc.runtime.txedit.model.TxR;
import com.step.jdbc.runtime.param.UserTransaction;
import com.step.jdbc.runtime.txedit.model.enums.EditType;
import com.step.jdbc.runtime.param.JdbcParam;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface TransactionEditAPI {

    String TRANSACTION_KEY = "TRANSACTION:USER:%s:PROGRAM_CODE:%s:%s";
    String EXECUTE_SUFFIX = "_EXECUTE_EDIT";
    TransactionOption DEF_OPTIONS = new TransactionOption();

    /**
     * 获取用户事务锁,对数据主键开启事务
     *
     * @param account     账号
     * @param programCode 程序号
     */
    default String getTranKey(String account, String programCode) {
        return String.format(TRANSACTION_KEY, account, programCode, UUID.randomUUID());
    }

    /**
     * 获取用户行为锁
     * 用户的编辑操作原子性,防止操作未结束而在此操作
     *
     * @param tranKey 事务锁
     */
    default String getExecuteKey(String tranKey) {
        return tranKey + EXECUTE_SUFFIX;
    }

    /**
     * 开启编辑功能,对数据进行锁定
     */
    TxR beginEdit(String lockIp, String account, String programCode, String dataKey, String lockSql, JdbcParam jdbcParam, EditType editType);

    default TxR beginEdit(String lockIp, String account, String programCode, String dataKey, List<String> lockSqls, JdbcParam jdbcParam, EditType editType) {
        return beginEdit(lockIp, account, programCode, dataKey, concatSql(lockSqls), jdbcParam, editType);
    }

    default TxR beginEdit(UserTransaction userTransaction, JdbcParam jdbcParam) {
        if (userTransaction == null || jdbcParam == null) {
            throw new BaseException("userTransaction | jdbcParam 参数不能为空").record();
        }
        return beginEdit(
                userTransaction.getLockIp(),
                userTransaction.getAccount(),
                userTransaction.getProgramCode(),
                userTransaction.getDataKey(),
                concatSql(userTransaction.getLockSqls()),
                jdbcParam,
                userTransaction.getEditType()
        );
    }

    /**
     * 用户更新数据的操作,要将操作调取带有事务状态的服务器处理
     *
     * @param programCode 程序代码
     * @param dataKey     数据主键
     */
    TxR updateData(String lockIp, String account, String programCode, String oldDataKey, String dataKey, String lockSql, TransactionSql transactionSql, TransactionOption options);

    default TxR updateData(String lockIp, String account, String programCode, String oldDataKey, String dataKey, String lockSql, TransactionSql transactionSql) {
        return updateData(lockIp, account, programCode, oldDataKey, dataKey, lockSql, transactionSql, DEF_OPTIONS);
    }

    default TxR updateData(UserTransaction userTransaction, TransactionSql transactionSql, TransactionOption options) {
        if (userTransaction == null) {
            throw new BaseException(ServerCode.TX_INFO_EMPTY.getMessage()).record();
        }
        return updateData(
                userTransaction.getLockIp(),
                userTransaction.getAccount(),
                userTransaction.getProgramCode(),
                userTransaction.getOldDataKey(),
                userTransaction.getDataKey(),
                concatSql(userTransaction.getLockSqls()),
                transactionSql,
                options);
    }


    default TxR updateData(UserTransaction userTransaction, TransactionSql transactionSql) {
        return updateData(userTransaction, transactionSql, DEF_OPTIONS);
    }

    /**
     * 更新但不提交事务
     * 一般用于第三方接口更新调用
     *
     * @param lockIp         用户ip
     * @param account        用户账号
     * @param programCode    程序号
     * @param dataKey        数据主键
     * @param transactionSql 更新sql
     */
    TxR updateDataNonCommit(String lockIp, String account, String programCode, String dataKey, TransactionSql transactionSql);

    default TxR updateDataNonCommit(UserTransaction userTransaction, TransactionSql transactionSql) {
        return updateDataNonCommit(userTransaction.getLockIp(), userTransaction.getAccount(), userTransaction.getProgramCode(), userTransaction.getDataKey(), transactionSql);
    }

    /**
     * 拼接sql信息
     *
     * @param sqls sql集合
     */
    static String concatSql(List<String> sqls) {
        if (CollectionUtils.isEmpty(sqls)) {
            throw new BaseException(ServerCode.SQL_IS_EMPTY.getMessage()).record();
        }
        return sqls.stream().filter(s -> !StringUtils.isEmpty(s)).map(sql -> {
            sql = sql.trim();
            return sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql;
        }).collect(Collectors.joining(";\n")) + ";";
    }

    TxR exitEdit(String lockIp, String account, String programCode, String dataKey, boolean isCommit, TransactionOption options);

    default TxR exitEdit(String lockIp, String account, String programCode, String dataKey, boolean isCommit) {
        return exitEdit(lockIp, account, programCode, dataKey, isCommit, DEF_OPTIONS);
    }


    default TxR exitEdit(UserTransaction userTransaction, boolean isCommit) {
        return exitEdit(userTransaction, isCommit, DEF_OPTIONS);
    }

    default TxR exitEdit(UserTransaction userTransaction, boolean isCommit, TransactionOption options) {
        if (userTransaction == null) {
            throw new BaseException("userTransaction 参数不能为空").record();
        }
        return exitEdit(
                userTransaction.getLockIp(),
                userTransaction.getAccount(),
                userTransaction.getProgramCode(),
                userTransaction.getDataKey(),
                isCommit
                , options);
    }

}
