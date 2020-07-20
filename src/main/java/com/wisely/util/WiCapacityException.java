package com.wisely.util;

public class WiCapacityException extends WiException {

	public WiCapacityException(String msg) {
		super(msg);
	}

	public WiCapacityException() {
		super("Over capacity at this time");
	}

}
