/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wisely.scheduling.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wisely.scheduling.dao.AvailabilityDao;
import com.wisely.scheduling.model.WiApiAvailability;
import com.wisely.scheduling.model.WiApiAvailabilitySlot;
import com.wisely.scheduling.model.WiDiner;
import com.wisely.scheduling.model.WiRestaurant;
import com.wisely.util.WiCapacityException;
import com.wisely.util.WiException;

/**
 * API test for the reservation scheduler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
         "classpath:META-INF/config/applicationContextServices.xml"
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReservationSchedulerTest  {
	
	@Autowired
	private ReservationScheduler reservationScheduler;
    
	@Test
	public void AsaveAvailability() throws Exception {		
		
		try {
			WiRestaurant restaurant = new WiRestaurant();
			restaurant.setName("Mickey Dees");
			restaurant.setTz("US/Eastern");
			
			// restaurant opens at noon and closes 1PM
			ZonedDateTime startTime = ZonedDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
			ZonedDateTime endTime = startTime.plusHours(3); // 1 AM tomorrow
			
			WiApiAvailability availability = new WiApiAvailability();
			availability.setStartTime(startTime);
			availability.setEndTime(endTime);
			availability.setRestaurant(restaurant);
			
			availability
				.addSlot(new WiApiAvailabilitySlot(startTime, 1))
				.addSlot(new WiApiAvailabilitySlot(startTime.plusHours(1), 8))  // 1pm
				.addSlot(new WiApiAvailabilitySlot(startTime.plusHours(2), 2)); // 2pm till 3
			
			
			reservationScheduler.setAvailability(restaurant, availability);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WiException(e);
		}
		
    }
	
	@Test
	public void BmakeReservation() throws Exception {
		try {
			WiRestaurant restaurant = new WiRestaurant();
			restaurant.setName("Mickey Dees");
			restaurant.setTz("US/Eastern");
			
			LocalDate today = LocalDate.now();


			List<ZonedDateTime> availableTimes = reservationScheduler.getAvailability(restaurant, today);
			
			// log them out
			availableTimes.forEach(dt -> System.out.println(dt) );
			
			WiDiner diner = new WiDiner("Jerry Thomas", "jerry@jjthomas.org");
			
			// make a reservation at 12:15
			reservationScheduler.makeReservation(restaurant, availableTimes.get(1), diner, 2);
			
			// try again (should fail)
			try {
				reservationScheduler.makeReservation(restaurant, availableTimes.get(1), diner, 2);
				
				throw new WiException("capacity check failed");
				
			} catch (WiCapacityException ex) {
				// do nothing / expected behavior
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WiException(e);
		}
	}
    
}