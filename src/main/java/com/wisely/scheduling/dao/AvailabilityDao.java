package com.wisely.scheduling.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.wisely.scheduling.model.WiAvailability;
import com.wisely.scheduling.model.WiRestaurant;
import com.wisely.util.WiException;

/**
 * Stores in memory for now.
 * 
 * @author JThomas1
 *
 */
@Component
public class AvailabilityDao implements InitializingBean {
	
	private Map<String, WiAvailability> store;

	@Override
	public void afterPropertiesSet() throws Exception {
		store = new HashMap<String, WiAvailability>();
	}
	
	public void saveAvailability(WiAvailability a) {
		store.put(a.getRestaurant().getName(), a);
	}
	
	public WiAvailability getAvailability(WiRestaurant r) throws WiException {
		WiAvailability a = null;
		if (store.containsKey(r.getName())) {
			a = store.get(r.getName());
		}
		if (a==null) {
			throw new WiException("No availability configured");
		}
		return a;
	}
}
