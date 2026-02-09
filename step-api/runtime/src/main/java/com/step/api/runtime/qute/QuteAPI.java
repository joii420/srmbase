package com.step.api.runtime.qute;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/1/3  14:42
 */
public interface QuteAPI {

    String getTempSql(String sql, Map<String,Object> args);

    String getTempSql(String sql, QuteArg args);


}
