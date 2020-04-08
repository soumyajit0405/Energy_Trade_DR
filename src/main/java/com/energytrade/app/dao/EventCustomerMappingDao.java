package com.energytrade.app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.energytrade.app.model.EventCustomerDevices;
import com.energytrade.app.model.EventCustomerMapping;
import com.energytrade.app.model.UserDRDevices;
import com.energytrade.app.model.UserDevice;
import com.energytrade.app.util.CustomMessages;

@Transactional
@Repository
public class EventCustomerMappingDao extends AbstractBaseDao {

	@Autowired
	EventCustomerMappingRepository eventCustomerMappingRepo;

	@Autowired
	UserDevicesRepository userDeviceRepository;

	@Autowired
	EventCustomerDevicesRepository eventCustomerDevicesRepository;

	public HashMap<String, Object> participateInEvent(HashMap<String, Object> inputDetails) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		int userId = (int) inputDetails.get("userId");
		int eventId = (int) inputDetails.get("eventId");
		double committedPower = (double) inputDetails.get("committedPower");
		ArrayList<Integer> deviceList = (ArrayList<Integer>) inputDetails.get("devices");

		try {
			eventCustomerMappingRepo.participateInEvent(userId, eventId, committedPower);
			ArrayList<EventCustomerDevices> eventCustomerDevicesList = new ArrayList<EventCustomerDevices>();
			EventCustomerMapping eventCustomerMapping = eventCustomerMappingRepo.getEventCustomerMapping(eventId,
					userId);
			for (int i = 0; i < deviceList.size(); i++) {
				EventCustomerDevices eventCustomerDevices = new EventCustomerDevices();
				UserDRDevices userDrDevice = userDeviceRepository.getUserDRDevice(deviceList.get(i));
				eventCustomerDevices.setUserDrDevice(userDrDevice);
				eventCustomerDevices.setEventCustomerMapping(eventCustomerMapping);
				eventCustomerDevicesList.add(eventCustomerDevices);

			}
			eventCustomerDevicesRepository.saveAll(eventCustomerDevicesList);
			response.put("message", CustomMessages.getCustomMessages("SUC"));
			response.put("key", "200");
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");
		}
		return response;
	}

	public HashMap<String, Object> withdrawFromEvent(int userId, int eventId) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			eventCustomerMappingRepo.withdrawFromEvent(userId, eventId);
			response.put("message", CustomMessages.getCustomMessages("SUC"));
			response.put("key", "200");
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");
		}
		return response;
	}

	public HashMap<String, Object> updateEventCommitments(int userId, int eventId, double updatedCommitedPower,
			double updatedBidPrice) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			eventCustomerMappingRepo.updateEventCommitments(userId, eventId, updatedCommitedPower, updatedBidPrice);
			response.put("message", CustomMessages.getCustomMessages("SUC"));
			response.put("key", "200");
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");
		}
		return response;
	}

}
