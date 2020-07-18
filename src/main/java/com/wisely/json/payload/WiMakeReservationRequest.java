package com.wisely.json.payload;

import java.time.ZonedDateTime;

import com.wisely.scheduling.model.WiDiner;
import com.wisely.scheduling.model.WiRestaurant;

public class WiMakeReservationRequest {
	public WiRestaurant restaurant;
	public ZonedDateTime dateTime;
	public WiDiner diner;
	public Integer partySize;
}
