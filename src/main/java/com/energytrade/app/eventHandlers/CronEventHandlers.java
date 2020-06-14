package com.energytrade.app.eventHandlers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CronEventHandlers {

	@KafkaListener(topics = "Gate_Closed", groupId = "group_id")
	public void gateClosedEventHandler(String message) {
		System.out.println("Gate-closed Event recieved: " + message);
	}
	
	@KafkaListener(topics = "Event_Locked", groupId = "group_id")
	public void eventLockedEventHandler(String message) {
		System.out.println("Locked Event recieved: " + message);
	}
	
	@KafkaListener(topics = "Event_Live", groupId = "group_id")
	public void EventHandler(String message) {
		System.out.println("Live Event recieved: " + message);
	}
	
	@KafkaListener(topics = "Event_Completed", groupId = "group_id")
	public void eventCompletedEventHandler(String message) {
		System.out.println("Completed Event recieved: " + message);
	}
	
	@KafkaListener(topics = "Event_Expired", groupId = "group_id")
	public void eventExpiredEventHandler(String message) {
		System.out.println("Expired Event recieved: " + message);
	}

}
