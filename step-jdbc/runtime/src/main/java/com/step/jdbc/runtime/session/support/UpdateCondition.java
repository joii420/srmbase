package com.step.jdbc.runtime.session.support;

/**
 * @author : Sun
 * @date : 2024/4/24  15:14
 */
public class UpdateCondition<T> {

    private T update;
    private T condition;

    public UpdateCondition(T update, T condition) {
        this.update = update;
        this.condition = condition;
    }

    public T getUpdate() {
        return update;
    }

    public void setUpdate(T update) {
        this.update = update;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }
}
