package com.wisely.rest.api;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.wisely.bean.ApplicationContextProvider;
import com.wisely.json.payload.WiInventoryResponse;
import com.wisely.json.payload.WiMakeReservationRequest;
import com.wisely.scheduling.model.WiApiAvailability;
import com.wisely.scheduling.model.WiAvailability;
import com.wisely.scheduling.model.WiRestaurant;
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
		try {
			getScheduler().setAvailability(availability);
		} catch (Exception e) {
			throw handleError(e);
		}
	}
	
	@Path("availabilities/{restaurant}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public WiAvailability getAvailability(@PathParam("restaurant") String restname) throws WiException {
		try {
			WiRestaurant r = new WiRestaurant();
			r.setName(restname);
			return getScheduler().getAvailability(r);
		} catch (Exception e) {
			throw handleError(e);
		}
	}
	
	/**
	 * 
	 * @param restname
	 * @param dateString MM:DD
	 * @return
	 * @throws WiException
	 */
	@Path("availabilities/{restaurant}/{year}/{month}/{day}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ZonedDateTime> getAvailability(@PathParam("restaurant") String restname, 
												@PathParam("year") int year,
												@PathParam("month") int month,
												@PathParam("day") int day) throws WiException {
		try {
			WiRestaurant r = new WiRestaurant();
			r.setName(restname);

			LocalDate date = LocalDate.of(year, month, day);

			return getScheduler().getAvailability(r, date);
		} catch (Exception e) {
			throw handleError(e);
		}
	}
	
	/**
	 * 
	 * @param restname
	 * @param dateString MM:DD
	 * @return
	 * @throws WiException
	 */
	@Path("inventory/{restaurant}/{year}/{month}/{day}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public WiInventoryResponse getInventory(@PathParam("restaurant") String restname, 
												@PathParam("year") int year,
												@PathParam("month") int month,
												@PathParam("day") int day) throws WiException {
		try {
			WiRestaurant r = new WiRestaurant();
			r.setName(restname);

			LocalDate date = LocalDate.of(year, month, day);

			int count = getScheduler().getInventory(r, date);
			
			WiInventoryResponse resp = new WiInventoryResponse();
			resp.date = date;
			resp.inventory = count;
			
			return resp;
		} catch (Exception e) {
			throw handleError(e);
		}
	}
	
	@Path("reservations")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void makeReservation(WiMakeReservationRequest req) throws WiException {
		try {
			getScheduler().makeReservation(req.restaurant, req.dateTime, req.diner, req.partySize);
		} catch (Exception e) {
			throw handleError(e);
		}
	}
	
	
	private ReservationScheduler getScheduler() {
		if (this.reservationScheduler==null) {
			reservationScheduler = ApplicationContextProvider.singleton.getApplicationContext().getBean(ReservationScheduler.class);
		}
		return reservationScheduler;
	}
}