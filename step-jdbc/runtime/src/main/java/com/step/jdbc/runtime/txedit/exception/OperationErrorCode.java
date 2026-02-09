package com.step.jdbc.runtime.txedit.exception;


import com.step.api.runtime.core.IResult;

public enum OperationErrorCode implements IResult {
    /**
     * code <= 9999
     * 操作异常
     */
    INVALID_PROGRAM("TRX_001", "无效的程序码或资料数据"),
    DATA_LOCKED("TRX_001", "数据资料已被锁定,请稍后再试;"),
    INVALID_USER("TRX_001", "无效的用户信息;"),
    SQL_LOCK_IS_NULL("TRX_001", "无效的参数 : sqlLock is null;"),
    INVALID_SQL_LOCK("TRX_001", "无效的参数 : sqlLock 必须包含 for update nowait;"),
    INVALID_EDIT("TRX_001", "非法编辑操作!"),
    ERROR_EDIT("TRX_001", "编辑异常 : 锁定数据异常!!! 请联系管理员"),
    ERROR_EDIT_DATA_EXPIRED("TRX_001", "编辑异常 : 数据已被更新!!! 请刷新数据后重试"),
    ERROR_EDIT_COMMIT_FAILED("TRX_001", "编辑异常 : 提交数据失败!!!"),
    INVALID_TRANSACTION("TRX_001", "编辑异常 : 找不到有效的事务!!!"),
    INVALID_EDIT_TYPE("TRX_001", "无效的编辑类型 : edit type is null !!!"),
    INVALID_JDBC_SOURCE("TRX_001", "无效的数据源 : jdbc source is null !!!"),
    ERROR_EXIT("TRX_001", "提交数据异常!!!"),
    MAPPER_HAS_NOT_JDBC("TRX_001", "Mapper模式不支持返回Jdbc"),
    UN_SUPPORT("TRX_001", "暂不支持 MapperEdit"),
    MISS_PARAM("TRX_001", "缺少参数"),
    BUSY("TRX_001", "操作频繁: 数据更新中~~! 请稍后再试"),
    ERROR_EXECUTE_SQL("TRX_001", "SQL执行异常"),

    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    OperationErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * code编码
     */
    final String code;
    /**
     * 中文信息描述
     */
    final String message;
}
