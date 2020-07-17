package com.wisely.scheduling.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.wisely.util.WiException;

/**
 * Created from WiApiAvailability with date time data replaced with 15 minutes slots.
 * For example an eight hour day starting at noon is represented with a '12:00' start time (HTML 5 time value)
 * with 8 * 4 15 minute slots (32 slots).  Capacity or the allowed # of reservations is stored for each slot.
 * 
 * @author JThomas1
 *
 */
public class WiAvailability {
	
	private WiRestaurant restaurant;
	
	private int startHour;
	private int startMinute;
	
	private List<WiAvailabilitySlot> slots;
	
	public WiAvailability(WiApiAvailability av) throws WiException {
		this.restaurant = av.getRestaurant();

		this.startHour = av.getStartTime().getHour();
		this.startMinute = av.getStartTime().getMinute();
		
		this.slots = new ArrayList<WiAvailabilitySlot>();
		
		createSlots(av);
	}
	
	private void createSlots(WiApiAvailability av) throws WiException {
		
		this.slots.clear();
		
		if (!av.getStartTime().isEqual(av.getSlots().get(0).getStartTime())) {
			throw new WiException("Start time mismatch");
		}
			
		int slotIndex = 0;
		ZonedDateTime time = av.getStartTime();
		
		for (int i=0; i<av.getSlots().size(); i++) {
			WiApiAvailabilitySlot slot = av.getSlots().get(i);
			WiApiAvailabilitySlot next = ((i+1) < av.getSlots().size()) ? av.getSlots().get(i+1) : null;
			ZonedDateTime slotEndTime = next!=null ? next.getStartTime() : av.getEndTime();
			
			while (time.isBefore(slotEndTime)) {
				WiAvailabilitySlot newslot = new WiAvailabilitySlot(this, slotIndex, slot.getCapacity());
				this.slots.add(newslot);
				time = time.plusMinutes(15);
			}
			
		}

	}
	
	/**
	 * Translate a dateTime into a slot index
	 * @param dateTime
	 * @return
	 * @throws WiException
	 */
	public int findSlotIndex(ZonedDateTime dateTime) throws WiException {
		
		// convert dateTime to a date and a slot for that date
		LocalDate dt = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
		
		// translate start time into zoned datetime for given date
		LocalTime lt = LocalTime.of(this.getStartHour(), this.getStartMinute());
		
		// set time to the start time
		ZonedDateTime time = ZonedDateTime.of(dt, lt, ZoneId.of(restaurant.getTz()));
		
		int slotIndex = 0;
		while (!time.isEqual(dateTime)) {
			slotIndex++;
			time = time.plusMinutes(15);
			
			// something wrong with time passed back to us
			if (time.isAfter(dateTime)) {
				throw new WiException("Invalid dateTime Value");
			}
		}
		return slotIndex;
	}

	public WiRestaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(WiRestaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<WiAvailabilitySlot> getSlots() {
		return slots;
	}

	public void setSlots(List<WiAvailabilitySlot> slots) {
		this.slots = slots;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

}
