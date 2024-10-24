package com.zsh.task.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {
    public static final CustomDateSerializer instance= new CustomDateSerializer();

    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (date == null) {
            gen.writeNull();
        }else {
            String format = dataFormat.format(date);
            gen.writeString(format);
        }

    }
}
