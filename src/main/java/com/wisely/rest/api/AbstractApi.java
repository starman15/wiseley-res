package com.wisely.rest.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

public class AbstractApi {
	
	static Logger logger = Logger.getLogger(AbstractApi.class);

	public AbstractApi() {
		super();
	}

	protected WebApplicationException handleError(Throwable t) {
		return handleError(null, t);
	}

	private WebApplicationException handleError(String msg, Throwable handledEx) {
		
		
	    String errMsg = handledEx.getMessage();
	    if (errMsg==null) {
	    	errMsg = "An internal error occurred";
	    }
	    Map<String,Object> errorResult = new HashMap<String,Object>();
	    errorResult.put("code", Status.BAD_REQUEST);
	    errorResult.put("message", errMsg);
	    
		Response resp = Response.ok(errorResult)
				.status(Status.BAD_REQUEST)
				.build();
		WebApplicationException ex = new WebApplicationException(resp);
		
		return ex;
	}

}