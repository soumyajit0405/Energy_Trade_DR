package com.energytrade.app.dto;

import java.util.List;

public class AllEventSetDto extends AbstractBaseDto{

	private int eventSetId;
	private String eventSetName;
	private String eventSetStatus;
	private int userId;
	private String userName;
	private List<AllEventDto> allEvents;
	private String dateOfOccurence;
	private String plannedPower;
	private String actualPower;
	private String totlaPrice;
	private String publishedEvents;
	private String completedEvents;
	private String cancelledEvents;
	public List<AllEventDto> getAllEvents() {
		return allEvents;
	}
	public void setAllEvents(List<AllEventDto> allEvents) {
		this.allEvents = allEvents;
	}
	public int getEventSetId() {
		return eventSetId;
	}
	public String getDateOfOccurence() {
		return dateOfOccurence;
	}
	public void setDateOfOccurence(String dateOfOccurence) {
		this.dateOfOccurence = dateOfOccurence;
	}
	public String getPlannedPower() {
		return plannedPower;
	}
	public void setPlannedPower(String plannedPower) {
		this.plannedPower = plannedPower;
	}
	public String getActualPower() {
		return actualPower;
	}
	public void setActualPower(String actualPower) {
		this.actualPower = actualPower;
	}
	public String getTotlaPrice() {
		return totlaPrice;
	}
	public void setTotlaPrice(String totlaPrice) {
		this.totlaPrice = totlaPrice;
	}
	public String getPublishedEvents() {
		return publishedEvents;
	}
	public void setPublishedEvents(String publishedEvents) {
		this.publishedEvents = publishedEvents;
	}
	public String getCompletedEvents() {
		return completedEvents;
	}
	public void setCompletedEvents(String completedEvents) {
		this.completedEvents = completedEvents;
	}
	public String getCancelledEvents() {
		return cancelledEvents;
	}
	public void setCancelledEvents(String cancelledEvents) {
		this.cancelledEvents = cancelledEvents;
	}
	public void setEventSetId(int eventSetId) {
		this.eventSetId = eventSetId;
	}
	public String getEventSetName() {
		return eventSetName;
	}
	public void setEventSetName(String eventSetName) {
		this.eventSetName = eventSetName;
	}
	public String getEventSetStatus() {
		return eventSetStatus;
	}
	public void setEventSetStatus(String eventSetStatus) {
		this.eventSetStatus = eventSetStatus;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
