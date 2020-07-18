package com.wisely.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.wisely.bean.ApplicationContextProvider;
import com.wisely.rest.payload.WiAvailabilityRequest;
import com.wisely.scheduling.service.ReservationScheduler;
import com.wisely.util.WiException;

@Path("reservation")
public class ReservationApi extends AbstractApi implements InitializingBean {
	
	static Logger logger = Logger.getLogger(ReservationApi.class);
	
	private ReservationScheduler reservationScheduler;

	@Override
	public void afterPropertiesSet() throws Exception {
		reservationScheduler = ApplicationContextProvider.singleton.getApplicationContext().getBean(ReservationScheduler.class);
		
	}

	@Path("availabilities")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void setAvailability(WiAvailabilityRequest availability) throws WiException {
		reservationScheduler.setAvailability(availability.restaurant, availability.availability);
	}
}