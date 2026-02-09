package com.step.jdbc.runtime.session;




import java.util.Map;


public abstract class BaseJdbcEntity {

    public BaseJdbcEntity() {
    }

    protected abstract void initFromMap(Map<String, Object> map);


}
