package com.step.api.runtime.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2022/12/15  15:23
 */
public enum MicroRegister {
    /**
     * 微服务总控制
     */
    DEV("DEV", "开发者模式"),
    LOCAL("LOCAL", "本地模式"),
//    CENTRAL("TEST_CENTRAL", "控制中心"),
//    GATEWAY("TEST_GATEWAY", "网关服务器"),

    CENTRAL("CENTRAL", "控制中心"),
    GATEWAY("GATEWAY_SERVER", "网关服务器"),
    QUARTZ("QUARTZ_SERVER", "定时任务服务器"),
    LOGGER("LOGGER_SERVER", "日志服务器"),
    FILE("FILE_SERVER", "文件服务器"),

    /**
     * 事务管理服务器
     */
    TX_EDIT_1("TX_EDIT_1", "事务服务器-1"),
    TX_EDIT_2("TX_EDIT_2", "事务服务器-2"),
    TX_EDIT_3("TX_EDIT_3", "事务服务器-3"),


    ;
    private final String serverName;
    private final String desc;

    MicroRegister(String serverName, String desc) {
        this.serverName = serverName;
        this.desc = desc;
    }

    public String getServerName() {
        return serverName;
    }

    public String getDesc() {
        return desc;
    }

    public static List<String> micros() {
        MicroRegister[] values = MicroRegister.values();
        return Arrays.stream(values).sequential().map(MicroRegister::getServerName).collect(Collectors.toList());
    }

    /**
     * 获取事务服务器的微服务列表
     */
    public static List<String> transactionServerList(String microGroup) {
        return Arrays.stream(values()).map(MicroRegister::getServerName).filter(name -> name.startsWith("TX_EDIT_")).map(s -> microGroup + "." + s).collect(Collectors.toList());
    }
}
