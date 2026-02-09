package com.step.jdbc.runtime.session.converter;

import com.step.tool.utils.DateUtil;
import com.step.tool.utils.JsonUtil;
import com.step.tool.utils.StringUtil;
import org.postgresql.util.PGobject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

public class DefaultConverter implements ResultConverter {

    private static final String DATE_COLUMN_TYPE_NAME = "date";

    private static final String TIMESTAMP_TYPE_NAME = "timestamp";

    private static final String TIMESTAMPTZ_TYPE_NAME = "timestamptz";

    private static final String TIME_TYPE_NAME = "time";

    private static final String TIMETZ_TYPE_NAME = "timetz";

    @Override
    public Object convert(String type, Object value) {
        value = dateAndTimeConvert(value);
        value = bigDecimalToDouble(value);
        value = bigIntegerToLong(value);
        value = jsonbToString(value);
        return value;
    }

    private Object jsonbToString(Object value) {
        if (value instanceof PGobject jsonb) {
            return jsonb.getValue();
//            if (StringUtil.isNotEmpty(jsonbValue)) {
//                return JsonUtil.parse(jsonbValue, Map.class);
//            }
        }
        return value;
    }

    private Object dateAndTimeConvert(Object value) {
        if (value instanceof Date date) {
            return DateUtil.formatDate(date);
        } else if (value instanceof Time time) {
            return DateUtil.formatTime(time);
        } else if (value instanceof Timestamp timestamp) {
            return DateUtil.formatDateTime(timestamp);
        }
        return value;
    }

    private Object bigDecimalToDouble(Object value) {
        if (value instanceof BigDecimal bigDecimal) {
            if (bigDecimal.doubleValue() == bigDecimal.intValue()) {
                return bigDecimal.intValue();
            }
            return bigDecimal.doubleValue();
        }
        return value;
    }

    private Object bigIntegerToLong(Object value) {
        if (value instanceof BigInteger bigInteger) {
            return bigInteger.longValue();
        }
        return value;
    }
}
