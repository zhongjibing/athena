package com.icezhg.athena.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by zhongjibing on 2020/04/27
 */
public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = ObjectMapperFactory.getObjectMapper();
    }

    private JsonUtil() {
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }

    public static String toJson(Object object, DateFormat dateFormat) {
        ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
        mapper.setDateFormat(dateFormat);

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T toBean(String json, Class<T> type) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T toBean(String json, TypeReference<T> typeRef) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T toBean(InputStream in, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(in, type);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T toBean(InputStream in, TypeReference<T> typeRef) {
        try {
            return OBJECT_MAPPER.readValue(in, typeRef);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    private static class ObjectMapperFactory {

        private static ObjectMapper getObjectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .registerModule(new SimpleModule());
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            return objectMapper;
        }
    }

    public static class JsonParseException extends RuntimeException {
        private static final long serialVersionUID = -5271479650559751840L;

        JsonParseException(Throwable cause) {
            super(cause);
        }
    }
}
