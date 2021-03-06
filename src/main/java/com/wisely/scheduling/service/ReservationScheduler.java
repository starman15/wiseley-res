package com.wisely.scheduling.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wisely.scheduling.dao.AvailabilityDao;
import com.wisely.scheduling.dao.ReservationDao;
import com.wisely.scheduling.model.WiApiAvailability;
import com.wisely.scheduling.model.WiAvailability;
import com.wisely.scheduling.model.WiAvailabilitySlot;
import com.wisely.scheduling.model.WiDiner;
import com.wisely.scheduling.model.WiReservation;
import com.wisely.scheduling.model.WiRestaurant;
import com.wisely.util.WiCapacityException;
import com.wisely.util.WiException;

@Component
public class ReservationScheduler {
	
	@Autowired
	private AvailabilityDao availabilityDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	/**
	 * Converts to dense version of WiApi and stores it.
	 * 
	 * @param restaurant
	 * @param availability
	 * @throws WiException 
	 */
	public void setAvailability(WiApiAvailability availability) throws WiException {
		
		// convert to internally used structure
		WiAvailability a = new WiAvailability(availability);
		availabilityDao.saveAvailability(a);
	}
	
	/**
	 * Used for troubleshooting
	 * @return
	 * @throws WiException 
	 */
	public WiAvailability getAvailability(WiRestaurant restaurant) throws WiException {
		
		WiAvailability availability = availabilityDao.getAvailability(restaurant);
		return availability;
	}
	
	/**
	 * For a given restaurant and date find the current available times.
	 * 
	 * @param restaurant
	 * @param date
	 * @return
	 * @throws WiException 
	 */
	public List<ZonedDateTime> getAvailability(WiRestaurant restaurant, LocalDate date) throws WiException {
		
		WiAvailability availability = availabilityDao.getAvailability(restaurant);
		
		// translate start time into zoned datetime for given date
		LocalTime lt = LocalTime.of(availability.getStartHour(), availability.getStartMinute());
		
		ZoneId restTz = ZoneId.of(availability.getRestaurant().getTz());
		ZonedDateTime time = ZonedDateTime.of(date, lt, restTz);
		
		
		// find out what the usage is currently
		List<Integer> usedCapacity = reservationDao.getUsedCapacity(restaurant, date, availability.getSlots().size());
		
		List<ZonedDateTime> daysAvailabilty = new ArrayList<ZonedDateTime>();
		
		// for each slot, check if it's used up or not
		for (WiAvailabilitySlot slot: availability.getSlots()) {
			
			if (slot.getCapacity() > usedCapacity.get(slot.getIndex())) {
				// there is room for another reservation
				int addMinutes = slot.getIndex()*15;
				ZonedDateTime resTime = time.plusMinutes(addMinutes).withZoneSameInstant(restTz);
				daysAvailabilty.add(resTime);
			}
		}
		
		// sort the list
//		daysAvailabilty = daysAvailabilty.stream()
//				.sorted((o1, o2)-> o1.compareTo(o2))
//                .collect(Collectors.toList());
		
		return daysAvailabilty;
	}
	
	/**
	 * Returns availability count for a date.
	 * 
	 * @param r
	 * @param date
	 * @return
	 * @throws WiException 
	 */
	public int getInventory(WiRestaurant restaurant, LocalDate date) throws WiException {
		WiAvailability availability = availabilityDao.getAvailability(restaurant);
		
		// translate start time into zoned datetime for given date
		LocalTime lt = LocalTime.of(availability.getStartHour(), availability.getStartMinute());
		
		ZoneId restTz = ZoneId.of(availability.getRestaurant().getTz());
		ZonedDateTime time = ZonedDateTime.of(date, lt, restTz);
		
		
		// find out what the usage is currently
		int availableCapacity = 0;
		for (WiAvailabilitySlot s: availability.getSlots())
			availableCapacity = availableCapacity + s.getCapacity();
		
		List<Integer> usedCapacity = reservationDao.getUsedCapacity(restaurant, date, availability.getSlots().size());
		int used = 0;
		for (Integer u: usedCapacity) {
			used = used + u;
		}
		
		return availableCapacity - used;	
	}
	
	/**
	 *
	 * @param restaurant
	 * @param dateTime assumed to be one of the time values returned from getAvailability
	 * @param diner
	 * @param partySize
	 * @throws WiException 
	 */
	public void makeReservation(WiRestaurant restaurant, ZonedDateTime dateTime, WiDiner diner, int partySize) throws WiException {
		
		WiAvailability availability = availabilityDao.getAvailability(restaurant);
		
		// browsers don't send TZ so add it
		int slotIndex = availability.findSlotIndex(dateTime);

		// find out what the usage is currently
		
		// before changing to localdate, make sure time is in restaurant's timezone
		ZonedDateTime rsDateTime = dateTime.withZoneSameInstant(ZoneId.of(availability.getRestaurant().getTz()));
		
		LocalDate dt = LocalDate.of(rsDateTime.getYear(), rsDateTime.getMonth(), rsDateTime.getDayOfMonth());
		List<Integer> usedCapacity = reservationDao.getUsedCapacity(restaurant, dt, availability.getSlots().size());
		
		if (usedCapacity.get(slotIndex) < availability.getSlots().get(slotIndex).getCapacity()) {
			// create and save the reservation
			WiReservation r = new WiReservation(dt, diner, slotIndex);
			r.setPartySize(partySize);
			r.setRestaurant(restaurant);
			
			reservationDao.saveReservation(r);
		} else {
			// cannot make reservation
			throw new WiCapacityException();
		}
	}
	

}
