package com.wisely.scheduling.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.wisely.scheduling.model.WiAvailability;
import com.wisely.scheduling.model.WiReservation;
import com.wisely.scheduling.model.WiRestaurant;
import com.wisely.util.WiException;

/**
 * Stores in memory for now.
 * 
 * @author JThomas1
 *
 */
@Component
public class ReservationDao implements InitializingBean {
	
	/**
	 * Simulate a date index in the database for each restaurant.
	 */
	private Map<String, Map<LocalDate, WiReservation>> store;

	@Override
	public void afterPropertiesSet() throws Exception {
		store = new HashMap<String, Map<LocalDate, WiReservation>>();
	}
	
	/**
	 * Returns used capacity for each time slot for that date.
	 * 
	 * Simulates a simple database query.
	 * 
	 * @param rest
	 * @param dt
	 * @param numSlots
	 * @return
	 */
	public List<Integer> getUsedCapacity(WiRestaurant rest, LocalDate dt, int numSlots) {
		List<Integer> used = new ArrayList<Integer>();
		
		// init to zero
		for (int i=0; i<numSlots; i++) {
			used.add(0);
		}
		
		if (store.containsKey(rest.getName())) {
			Map<LocalDate, WiReservation> allreservations = store.get(rest.getName());
			
			// find reservations on that date
			List<WiReservation> reservations = allreservations.keySet().stream()
														.filter(d -> dt.equals(dt))
														.map(d -> allreservations.get(d))
														.collect(Collectors.toList());
			reservations.forEach(r -> {
				int capacity = used.get(r.getSlotIndex()) + 1;
				used.set(r.getSlotIndex(), capacity);
			});
					
		}
		
		return used;
	}

	/**
	 * Save a reservation indexed by restaurant and date
	 * 
	 * @param r
	 */
	public void saveReservation(WiReservation r) {
		Map<LocalDate, WiReservation> reservations = store.get(r.getRestaurant().getName());
		
		if (reservations==null) {
			reservations = new HashMap<>();
			store.put(r.getRestaurant().getName(), reservations);
		}
		
		reservations.put(r.getDate(), r);		
	}
	

}
