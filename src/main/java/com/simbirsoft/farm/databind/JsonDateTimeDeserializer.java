package com.simbirsoft.farm.databind;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
 
        ObjectCodec oc = jp.getCodec();
        TextNode node = (TextNode) oc.readTree(jp);
        String dateString = node.textValue();
 
        Instant instant;
        try {
            instant = new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateString).toInstant();
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return dateTime;
        } catch (ParseException e) {
            return null;
        }
    }
}
