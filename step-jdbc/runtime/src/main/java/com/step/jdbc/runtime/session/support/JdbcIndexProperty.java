package com.step.jdbc.runtime.session.support;

import lombok.Data;

import java.util.List;

/**
 * @author : Sun
 * @date : 2024/9/11  15:42
 */
public class JdbcIndexProperty {

    private String name;
    private Boolean unique;
    private Boolean concurrently;
    private IndexType type;
    private List<String> columns;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getConcurrently() {
        return concurrently;
    }

    public void setConcurrently(Boolean concurrently) {
        this.concurrently = concurrently;
    }

    public IndexType getType() {
        return type;
    }

    public void setType(IndexType type) {
        this.type = type;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
