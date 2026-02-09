package com.step.qute.runtime;

import com.step.api.runtime.qute.QuteAPI;
import com.step.api.runtime.qute.QuteArg;
import io.quarkus.qute.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

@Singleton
public class TemplateSql implements QuteAPI {

    @Inject
    Engine engine;

    public static final Logger log = LoggerFactory.getLogger("QUTE-LOG");

    @Override
    public String getTempSql(String sql, Map<String, Object> args) {
        if (StringUtils.isEmpty(sql)) {
            // throw new SystemException("无效sql");
        }
        String render = "";
        try {
            QuteArg quteArg = maptoObj(args, QuteArg.class);
            return getTempSql(sql, quteArg);
        } catch (Exception e) {
            log.error("模板解析失败", e.getMessage());
            e.printStackTrace();
        }
        return render;
    }

    @Override
    public String getTempSql(String sql, QuteArg args) {
        try {
            return engine.parse(sql).data(args).render();
        } catch (Exception e) {
            log.error("模板解析失败", e.getMessage());
            return "";
        }
    }

    public static <T> T maptoObj(Map sourse, Class<T> target) throws InstantiationException, IllegalAccessException {
        Field[] fields = target.getDeclaredFields();
        T o = null;
            o = target.newInstance();

            Field[] var4 = fields;
            int var5 = fields.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Field field = var4[var6];
                Object val;

                if ((val = sourse.get(field.getName())) != null && field.getType() == BigDecimal.class){
                    field.setAccessible(true);
                    Object o1 = sourse.get(field.getName());
                    BigDecimal bigDecimal = null;
                    bigDecimal = (BigDecimal)o1;
                    field.set(o, bigDecimal);
                }
                else if ((val = sourse.get(field.getName())) != null&& val.getClass() == Timestamp.class ){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String parse = simpleDateFormat.format(val);
                    field.setAccessible(true);
                    field.set(o, parse);
                }
                else if (field.getType() == Long.class && (val = sourse.get(field.getName())) != null){
                    field.setAccessible(true);
                    field.set(o, Long.parseLong(sourse.get(field.getName()).toString()));
                }
                else if (field.getType() == Integer.class && (val = sourse.get(field.getName())) != null){
                    field.setAccessible(true);
                    field.set(o, Integer.parseInt(sourse.get(field.getName()).toString()));
                }
                else if (field.getType() == Float.class && (val = sourse.get(field.getName())) != null){
                    field.setAccessible(true);
                    field.set(o, ((Float)sourse.get(field.getName())).floatValue());
                }
                else if (field.getType() == Double.class && (val = sourse.get(field.getName())) != null){
                    field.setAccessible(true);
                    field.set(o, ((Double)sourse.get(field.getName())).doubleValue());
                }

                else if ((val = sourse.get(field.getName())) != null) {
                    field.setAccessible(true);
                    field.set(o, val);
                }
            }

            return o;

    }
}
