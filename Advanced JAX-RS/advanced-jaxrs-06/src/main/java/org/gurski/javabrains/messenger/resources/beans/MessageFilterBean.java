package org.gurski.javabrains.messenger.resources.beans;

import javax.ws.rs.QueryParam;

public class MessageFilterBean {
	
	private @QueryParam("year") int year;
	private @QueryParam("start") int start;
	private @QueryParam("size") int size;
	
	public int getYear() {
		return year;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getSize() {
		return size;
	}
}
