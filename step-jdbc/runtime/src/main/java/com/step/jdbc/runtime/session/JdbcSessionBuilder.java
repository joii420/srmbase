package com.step.jdbc.runtime.session;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.DBType;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.txedit.exception.JdbcError;
import com.step.api.runtime.exception.base.BaseException;
import com.step.logger.LOGGER;
import com.step.tool.utils.StringUtil;
import com.step.tool.utils.SystemUtil;
import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;

import java.sql.*;
import java.util.UUID;

/**
 * @author : Sun
 * @date : 2023/3/12  13:11
 */
public class JdbcSessionBuilder {

    private static String defUrl = SystemUtil.getValue("quarkus.datasource.jdbc.url");
    private static String defUsr = SystemUtil.getValue("quarkus.datasource.username");
    private static String defPwd = SystemUtil.getValue("quarkus.datasource.password");


    public static final Logger log = LoggerFactory.getLogger(JdbcSessionBuilder.class);

    /**
     * 初始化连接信息
     *
     * @param jdbcParam jdbc连接参数
     * @return
     */
    private static Connection initConnection(JdbcParam jdbcParam) {
        try {
            //连接成功
            return DriverManager.getConnection(jdbcParam.getUrl(), jdbcParam.getUsername(), jdbcParam.getPassword());
        } catch (SQLException e) {
            LOGGER.error(e, JdbcError.CON_003.getMessage());
            throw new BaseException(e);
        }
    }

    public static JdbcSession buildSession(JdbcParam jdbcParam, JdbcOption option, String poolName) {
        if (StringUtil.isEmpty(jdbcParam, jdbcParam.getUrl(), jdbcParam.getUsername(), jdbcParam.getPassword())) {
            throw new BaseException(JdbcError.CON_002.getMessage()).record();
        }
        if (option == null) {
            option = new JdbcOption();
        }
        Connection connection = initConnection(jdbcParam);
        try {
            if (option.getTransaction()) {
                //如果开启了事务,则不自动提交
                connection.setAutoCommit(false);
            }
            Statement statement = connection.createStatement();
            return new JdbcSession(UUID.randomUUID().toString().replace("-", ""), connection, statement, jdbcParam, option, poolName);
        } catch (SQLException e) {
            LOGGER.error(log, e, "Failed to create statement: ");
            throw new BaseException(e);
        } catch (Exception e) {
            LOGGER.error(log, e, "Failed to create statement: ");
            throw e;
        }
    }

    public static JdbcSession buildSession(JdbcParam jdbcParam, JdbcOption option) {
        return buildSession(jdbcParam, option, "COMMON");
    }

    public static JdbcSession buildSession(JdbcParam jdbcParam) {
        return buildSession(jdbcParam, new JdbcOption());
    }

    public static JdbcSession buildSession(JdbcOption option) {
        return buildSession(getSystemJdbcParam(), option);
    }

    public static JdbcSession buildSession() {
        return buildSession(new JdbcOption());
    }

    public static JdbcSessionPool buildSessionPool(JdbcParam jdbcParam, int poolSize) {
        return new JdbcSessionPool(jdbcParam, new JdbcOption(), poolSize);
    }

    public static JdbcSessionPool buildSessionPool(int poolSize) {
        return new JdbcSessionPool(getSystemJdbcParam(), new JdbcOption(), poolSize);
    }

    public static JdbcSessionPool buildSessionPool(JdbcParam jdbcParam, JdbcOption option, int poolSize) {
        return new JdbcSessionPool(jdbcParam, option, poolSize);
    }

    public static JdbcParam getSystemJdbcParam() {
        JdbcParam jdbcParam = new JdbcParam();
        jdbcParam.setUrl(defUrl);
        jdbcParam.setUsername(defUsr);
        jdbcParam.setPassword(defPwd);
        return jdbcParam;
    }
}
