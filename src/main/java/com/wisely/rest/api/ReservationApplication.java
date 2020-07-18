package com.wisely.rest.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.wisely.util.JavaMetaUtil;

@ApplicationPath("/api/v1")
public class ReservationApplication extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(JacksonConfig.class);
        classes.add(ReservationApi.class);
        classes.add(JacksonFeature.class);
        return classes;
	}
	
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("jersey.config.server.provider.packages", JavaMetaUtil.getPackagePath(JacksonConfig.class));
		return properties;
	}	
}