package com.wisely.scheduling.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * REST interface using ISO 8601 datetime data.  The scheduler converts this to WiAvailability which is used for
 * computing the schedule.
 * 
 * Provides a start time and this a list of capacities.
 * 
 * starttime 2020-07-14I12:00:00Z−05:00
 * endTime   2020-07-15T01:00:00Z−05:00  (1AM the next day)
 * 
 * {
 * 	  time: 2020-07-14I12:00:00,
 *    capacity: 4
 * }
 * {
 * 	  time: 2020-07-14I18:00:00,
 *    capacity: 8
 * }
 * {
 * 	  time: 2020-07-14I22:00:00,
 *    capacity: 4
 * }
 * Represents a restaurant with the following capacities:
 * - 4 reservations every 15 mins from 12PM till 6PM
 * - 8 from 6PM till 10PM
 * - 4 from 10PM till 1AM * 
 * 
 * @author JThomas1
 *
 */
public class WiApiAvailability {
	
	private WiRestaurant restaurant;
	
	private ZonedDateTime startTime;
	
	private ZonedDateTime endTime;
	
	private List<WiApiAvailabilitySlot> slots;
	
	public WiApiAvailability() {
		slots = new ArrayList<WiApiAvailabilitySlot>();
	}
	/**
	 * Support builder syntax
	 * 
	 * @param slot
	 * @return
	 */
	public WiApiAvailability addSlot(WiApiAvailabilitySlot slot) {
		slot.setOwner(this);
		slots.add(slot);
		return this;
	}

	public WiRestaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(WiRestaurant restaurant) {
		this.restaurant = restaurant;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	public List<WiApiAvailabilitySlot> getSlots() {
		return slots;
	}
	public void setSlots(List<WiApiAvailabilitySlot> slots) {
		this.slots = slots;
	}

}
