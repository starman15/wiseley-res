package com.wisely.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.wisely.bean.ApplicationContextProvider;
import com.wisely.scheduling.model.WiApiAvailability;
import com.wisely.scheduling.service.ReservationScheduler;
import com.wisely.util.WiException;

@Path("reservation")
public class ReservationApi extends AbstractApi {
	
	static Logger logger = Logger.getLogger(ReservationApi.class);
	
	private ReservationScheduler reservationScheduler;

	@Path("availabilities")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void setAvailability(WiApiAvailability availability) throws WiException {
		getScheduler().setAvailability(availability);
	}
	
	private ReservationScheduler getScheduler() {
		if (this.reservationScheduler==null) {
			reservationScheduler = ApplicationContextProvider.singleton.getApplicationContext().getBean(ReservationScheduler.class);
		}
		return reservationScheduler;
	}
}