package com.step.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.step.tool.utils.DateUtil;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author : Sun
 * @date : 2023/10/23  8:43
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(DateUtil.formatDateTime(o));
    }
}
