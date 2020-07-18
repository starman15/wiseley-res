package com.wisely.json;

import javax.ws.rs.ext.Provider;

import com.wisely.util.NestableException;



@Provider
public class JsonParseException extends NestableException  {
	
	private static final long serialVersionUID = 1L;

	public JsonParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JsonParseException(String msg) {
		super(msg);
	}

	public JsonParseException() {
		super();
	}

	public JsonParseException(Throwable cause) {
		super(cause);
	}
	
}
