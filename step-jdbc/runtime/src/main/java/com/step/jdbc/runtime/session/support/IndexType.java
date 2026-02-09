package com.step.jdbc.runtime.session.support;

/**
 * @author : Sun
 * @date : 2024/9/11  15:25
 */
public enum IndexType {
    /**
     * 索引类型
     */
    B_TREE("btree"),
    HASH("hash"),
    GiST("gist"),
    GIN("gin"),
    SP_GIST("spgist"),
    BRIN("brin"),
    ;


    private final String value;

    IndexType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
