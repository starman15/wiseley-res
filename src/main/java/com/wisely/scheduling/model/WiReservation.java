package com.wisely.scheduling.model;

import java.time.LocalDate;

public class WiReservation {
	private WiRestaurant restaurant;
	private LocalDate date;
	private int slotIndex;
	private WiDiner diner;
	private Integer partySize;
	
	public WiReservation(LocalDate date, WiDiner diner, int slotIndex) {
		this.date = date;
		this.diner = diner;
		this.slotIndex = slotIndex;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public int getSlotIndex() {
		return slotIndex;
	}
	public void setSlotIndex(int slotIndex) {
		this.slotIndex = slotIndex;
	}
	public WiDiner getDiner() {
		return diner;
	}
	public void setDiner(WiDiner diner) {
		this.diner = diner;
	}
	public Integer getPartySize() {
		return partySize;
	}
	public void setPartySize(Integer partySize) {
		this.partySize = partySize;
	}
	public WiRestaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(WiRestaurant restaurant) {
		this.restaurant = restaurant;
	}
	
}
