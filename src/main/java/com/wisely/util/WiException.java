package com.wisely.util;

public class WiException extends NestableException {
	
    public WiException(String msg)
    {
        super(msg);
    }

    public WiException(Throwable cause)
    {
        super();
    }

	public WiException() {
	}
}
