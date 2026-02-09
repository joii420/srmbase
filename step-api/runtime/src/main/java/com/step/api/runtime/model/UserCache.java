package com.step.api.runtime.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 保存于缓存的用户权限模型
 * @author : Sun
 * @date : 2022/12/14  10:55
 */
public class UserCache implements Serializable {

    /**登录系统*/
    private String serveCode;
    /**登录客户端*/
    private String clientCode;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 用户工号
     */
    private String userCode;
    /**
     * 用户工号
     */
    private String deptCode;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户所有的据点权限和过期时间
     */
    private Map<String, Long> validSites;
    /**
     * 用户权限模型  key: siteCode
     */
    private Map<String, List<UserAuth>> userAuths;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getServeCode() {
        return serveCode;
    }

    public void setServeCode(String serveCode) {
        this.serveCode = serveCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, Long> getValidSites() {
        return validSites;
    }

    public void setValidSites(Map<String, Long> validSites) {
        this.validSites = validSites;
    }

    public Map<String, List<UserAuth>> getUserAuths() {
        return userAuths;
    }

    public void setUserAuths(Map<String, List<UserAuth>> userAuths) {
        this.userAuths = userAuths;
    }
}
