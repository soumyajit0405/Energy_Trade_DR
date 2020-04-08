package com.energytrade.app.model;

import com.energytrade.app.dto.AbstractBaseDto;

public class EventSetEventDto extends AbstractBaseDto {

	private int eventId;
	private String eventCustomerMappingStatus;
	private String plannedPower;
	private String plannedPrice;
	

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	

	public String getEventCustomerMappingStatus() {
		return eventCustomerMappingStatus;
	}

	public void setEventCustomerMappingStatus(String eventCustomerMappingStatus) {
		this.eventCustomerMappingStatus = eventCustomerMappingStatus;
	}

	public String getPlannedPower() {
		return plannedPower;
	}

	public void setPlannedPower(String plannedPower) {
		this.plannedPower = plannedPower;
	}

	public String getPlannedPrice() {
		return plannedPrice;
	}

	public void setPlannedPrice(String plannedPrice) {
		this.plannedPrice = plannedPrice;
	}

	
}
