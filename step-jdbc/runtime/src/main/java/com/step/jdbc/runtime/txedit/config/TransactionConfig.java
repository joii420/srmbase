package com.step.jdbc.runtime.txedit.config;

import com.step.jdbc.runtime.txedit.exception.OperationErrorCode;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.tool.utils.StringUtil;

public class TransactionConfig {
    /**
     * 事务状态超时时间 (秒)
     */
    public static final int TIME_OUT = 24 * 60 * 60;
    /**
     * 程序锁的统一前缀
     */
    private static final String PROGRAM_KEY_PREFIX = "LOCK:PROGRAM:";


    /**
     * 获取程序锁的key
     */
    public static String getProgramKey(String programCode, String dataKey) {
        if (StringUtil.isEmpty(programCode, dataKey)) {
            throw new BaseException(OperationErrorCode.INVALID_PROGRAM.getMessage()).record();
        }
        return PROGRAM_KEY_PREFIX + programCode + ":" + dataKey;
    }

    /**
     * 获取程序锁的key
     */
    public static String getProgramKey(LockParam lockParam) {
        return getProgramKey(lockParam.getProgramCode(), lockParam.getDataKey());
    }


}
