package com.wisely.scheduling.model;

public class WiDiner {
	private String name;
	private String email;
	
	public WiDiner(String n, String e) {
		this.name = n;
		this.email = e;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
