package com.wisely.rest.api;

import java.sql.Time;

import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wisely.json.TimeDeserializer;



public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public JacksonConfig() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Time.class, new TimeDeserializer());
        objectMapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return objectMapper;
    }
}
