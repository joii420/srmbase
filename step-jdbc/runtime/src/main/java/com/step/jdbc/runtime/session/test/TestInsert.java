package com.step.jdbc.runtime.session.test;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.session.anno.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : Sun
 * @date : 2024/4/23  20:04
 */
@JdbcTable("test_insert")
public class TestInsert {

//    @JdbcInsertIgnore
    private Long id;
    @JdbcDefaultValue("AAA")
    private TestEnum testEnum;
    @JdbcDefaultValue("nameDef")
    private String name;
    private String age;
    @JdbcColumn("create_time")
    @JdbcDateFormat(format = "yyyy-MM-dd")
    @JdbcDefaultValue("asd")
    private LocalDateTime ttt;
    @JdbcIgnore
    private String ignore;
    private BaseException exception;

    public TestEnum getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public LocalDateTime getTtt() {
        return ttt;
    }

    public void setTtt(LocalDateTime ttt) {
        this.ttt = ttt;
    }

    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }

    public BaseException getException() {
        return exception;
    }

    public void setException(BaseException exception) {
        this.exception = exception;
    }
}
