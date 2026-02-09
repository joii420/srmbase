package com.step.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Formatter;

/**
 * @author : Sun
 * @date : 2023/1/28  11:20
 */
public class LOGGER {

    public static Logger create(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 默认日志打印器
     */
    private static final Logger DEF_LOGGER = LoggerFactory.getLogger("DEF_LOGGER");
    private static final String DEFAULT_REMIND = "错误信息";

    public static void error(Logger logger, Exception e, String remind, Object... args) {
        if (e instanceof SQLException sqlException) {
            logger.error(" ErrorCode : {} ,SQLState :{} ,{}: {} {} ", sqlException.getErrorCode(), sqlException.getSQLState(), format(remind, args), sqlException.getMessage(), sqlException);
        } else {
            String message = e.getMessage();
            if (e.getMessage() == null && e.getCause() != null) {
                message = e.getCause().getMessage();
            }
            logger.error("{} :{} {} , ", format(remind, args), message, e);
        }
    }

    public static void error(Logger logger, Exception e) {
        error(logger, e, DEFAULT_REMIND);
    }

    public static void error(Exception e, String remind, Object... args) {
        error(DEF_LOGGER, e, remind, args);
    }

    public static void error(Exception e) {
        error(DEF_LOGGER, e, DEFAULT_REMIND);
    }

    public static void error(Logger logger, Throwable e, String remind, Object... args) {
        logger.error("{} :{} {} , ", format(remind, args), e.getMessage(), e);
    }

    public static void error(Logger logger, Throwable e) {
        error(logger, e, DEFAULT_REMIND);
    }

    public static void error(Throwable e, String remind, Object... args) {
        error(DEF_LOGGER, e, remind, args);
    }

    public static void error(Throwable e) {
        error(DEF_LOGGER, e, DEFAULT_REMIND);
    }

    public static void error(Logger logger, String msg, Object... args) {
        logger.error(format(msg, args));
    }

    public static void error(String msg, Object... args) {
        error(DEF_LOGGER, format(msg, args));
    }


    public static void info(Logger logger, String msg, Object... args) {
        logger.info(format(msg, args));
    }

    public static void info(String msg, Object... args) {
        info(DEF_LOGGER, format(msg, args));
    }

    public static void warn(Logger logger, String msg, Object... args) {
        logger.warn(format(msg, args));
    }

    public static void warn(String msg, Object... args) {
        warn(DEF_LOGGER, format(msg, args));
    }


    public static void debug(Logger logger, String msg, Object... args) {
        logger.debug(format(msg, args));
    }

    public static void debug(String msg, Object... args) {
        debug(DEF_LOGGER, format(msg, args));
    }

    private static String format(String msg, Object... args) {
        msg = msg == null ? "" : msg;
        if (args == null || args.length == 0) {
            return msg;
        }
        return new Formatter().format(msg, args).toString();
    }
}
