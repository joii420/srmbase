package com.step.api.runtime.file;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author : Sun
 * @date : 2022/10/8  8:27
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FileParam {

    /** pg 唯一id*/
    private String id;
    /**
     * 文件id  文件服务器/file?id=fileId&fileType=public
     */
    private String fileId;
    /**
     * 文件全程 包含 .txt 的尾缀
     */
    private String fileFullName;
    /**
     * 文件名称 不包含 .尾缀
     */
    private String fileName;
    /**
     * 文件尾缀 txt
     */
    private String fileSuffix;
    /**
     * 文件大小
     */
    private String size;
    /**
     * 系统码
     */
    private String serverCode;
    /**
     * 平台
     */
    private String platformCode;
    /**
     * 是否为公有文件
     */
        private Boolean publicFile;
    /**
     * 有效期
     */
    private Integer inDate;
    /**
     * 所属目录
     */
    private String catalog;
    /**
     * 存储类型  服务器存储/MongoDB存储
     */
    private String saveType;
    /**
     * 文件是否重命名
     * 用于判断文件是否有重名
     *
     */
    private String isRename;
    /**
     * 文件是否重命名
     * 用于判断文件是否有重名
     *
     */
    private List<String> usedName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileFullName() {
        return fileFullName;
    }

    public void setFileFullName(String fileFullName) {
        this.fileFullName = fileFullName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public Boolean getPublicFile() {
        return publicFile;
    }

    public void setPublicFile(Boolean publicFile) {
        this.publicFile = publicFile;
    }

    public Integer getInDate() {
        return inDate;
    }

    public void setInDate(Integer inDate) {
        this.inDate = inDate;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getIsRename() {
        return isRename;
    }

    public void setIsRename(String isRename) {
        this.isRename = isRename;
    }

    public List<String> getUsedName() {
        return usedName;
    }

    public void setUsedName(List<String> usedName) {
        this.usedName = usedName;
    }

    public FileParam() {
    }

    public FileParam(JsonObject jsonObject) {
        FileParamConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FileParamConverter.toJson(this, json);
        return json;
    }

}