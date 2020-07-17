package com.wisely.scheduling.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
	public void setAvailability(WiRestaurant restaurant, WiApiAvailability availability) throws WiException {
		
		// convert to internally used structure
		WiAvailability a = new WiAvailability(availability);
		availabilityDao.saveAvailability(a);
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
		
		ZonedDateTime time = ZonedDateTime.of(date, lt, ZoneId.of(restaurant.getTz()));
		
		// find out what the usage is currently
		List<Integer> usedCapacity = reservationDao.getUsedCapacity(restaurant, date, availability.getSlots().size());
		
		List<ZonedDateTime> daysAvailabilty = new ArrayList<ZonedDateTime>();
		
		// for each slot, check if it's used up or not
		for (WiAvailabilitySlot slot: availability.getSlots()) {
			
			if (slot.getCapacity() > usedCapacity.get(slot.getIndex())) {
				// there is room for another reservation
				ZonedDateTime resTime = time.plusMinutes(slot.getIndex()*15);
				daysAvailabilty.add(resTime);
			}
			
			time = time.plusMinutes(15);
		}
		
		return daysAvailabilty;
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
		
		int slotIndex = availability.findSlotIndex(dateTime);

		// find out what the usage is currently
		LocalDate dt = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getMinute());
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
