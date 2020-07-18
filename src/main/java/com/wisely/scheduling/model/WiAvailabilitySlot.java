package com.wisely.scheduling.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WiAvailabilitySlot {
	
	private WiAvailability owner;
	
	private int index;
	
	private int capacity;
	
	public WiAvailabilitySlot(WiAvailability o, int index, int cap) {
		this.owner = o;
		this.index = index;
		this.capacity = cap;
	}

	@JsonIgnore
	public WiAvailability getOwner() {
		return owner;
	}

	public void setOwner(WiAvailability owner) {
		this.owner = owner;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
