package com.step.jdbc.runtime.param;

import com.step.api.runtime.model.TransmitParam;
import com.step.jdbc.runtime.txedit.model.enums.EditType;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author : Sun
 * @date : 2023/1/12  13:37
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class UserTransaction {

    /**
     * 程序代码
     */
    private String programCode;
    /**
     * 锁定ip
     */
    private String lockIp;
    /**
     * 旧的数据主键
     */
    private String oldDataKey;
    /**
     * 数据主键
     */
    private String dataKey;
    /**
     * 事务编辑类型
     */
    private EditType editType;
    /**
     * 账号
     */
    private String account;
    /**
     * 用户工号
     */
    private String userCode;
    /**
     * 行锁sql
     */
    private List<String> lockSqls;

    public UserTransaction(TransmitParam param) {
        this.lockIp = param.getHostInfo().getIp();
        this.programCode = param.getProgramCode();
        this.editType = EditType.JDBC;
        this.account = param.getAccount();
        this.userCode = param.getUserCode();
    }

    public UserTransaction(JsonObject jsonObject) {
        UserTransactionConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        UserTransactionConverter.toJson(this, json);
        return json;
    }

    public String getLockIp() {
        return lockIp;
    }

    public void setLockIp(String lockIp) {
        this.lockIp = lockIp;
    }

    public List<String> getLockSqls() {
        return lockSqls;
    }

    public void setLockSqls(List<String> lockSqls) {
        this.lockSqls = lockSqls;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        this.editType = editType;
    }

    public String getOldDataKey() {
        return oldDataKey;
    }

    public void setOldDataKey(String oldDataKey) {
        this.oldDataKey = oldDataKey;
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
}
