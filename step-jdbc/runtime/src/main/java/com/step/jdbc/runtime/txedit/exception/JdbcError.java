package com.step.jdbc.runtime.txedit.exception;

import com.step.api.runtime.core.IResult;

/**
 * @author : Sun
 * @date : 2023/1/28  10:46
 */
public enum JdbcError implements IResult {
    /**
     * JDBC错误集
     */
    CON_001("CON_001", "无效的驱动"),
    CON_002("CON_001", "缺少连接参数"),
    CON_003("CON_001", "连接数据库失败..."),
    CON_004("CON_001", "执行sql失败..."),
    CON_005("CON_001", "错误的sql类型..."),
    CON_006("CON_001", "关闭连接失败..."),
    CON_007("CON_001", "该连接已失效..."),
    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    JdbcError(String code, String message) {
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
