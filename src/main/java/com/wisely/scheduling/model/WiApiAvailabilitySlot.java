package com.wisely.scheduling.model;

import java.time.ZonedDateTime;

/**
 * REST interface using ISO 8601 datetime data to make it more convenient to clients.
 * 
 * @author JThomas1
 *
 */
public class WiApiAvailabilitySlot {
	
	private WiApiAvailability owner;
	
	private ZonedDateTime startTime;
	
	private int capacity;
	
	public WiApiAvailabilitySlot() {
	}
	
	public WiApiAvailabilitySlot(ZonedDateTime st, int capacity) {
		this.startTime = st;
		this.capacity = capacity;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public WiApiAvailability getOwner() {
		return owner;
	}

	public void setOwner(WiApiAvailability owner) {
		this.owner = owner;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
