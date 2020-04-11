package com.energytrade.app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.EventCustomerMapping;
import com.energytrade.app.model.EventSetEventDto;
import com.energytrade.app.model.UserDRDevices;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.util.CustomMessages;
import com.energytrade.app.dto.AllEventSetDto;
import com.energytrade.app.dto.CustomerEventDetailsDto;
import com.energytrade.app.dto.CustomerEventMappingDetailsDto;
import com.energytrade.app.dto.DRDeviceDto;
import com.energytrade.app.dto.EventSetDetailsDto;
import com.energytrade.app.model.AllEvent;
import com.energytrade.app.model.AllEventSet;

@Transactional
@Repository
public class DRCustomerDao {

	@Autowired
	DRCustomerRepository drCustomerRepo;

	public HashMap<String, Object> getBusinessContractDetails(String contractNumber) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			response.put("contractDetails", drCustomerRepo.getBusinessContractDetails(contractNumber));
			response.put("userCount", drCustomerRepo.getUserDetailsfromContract(contractNumber));
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

	public HashMap<String, Object> updateDrUserDetails(String phone, String fullName, String drContractNumber) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			drCustomerRepo.updateDrUserDetails(fullName, drContractNumber, phone);
			AllUser alluser = drCustomerRepo.getUserIdByPhone(phone);
			response.put("message", CustomMessages.getCustomMessages("AS"));
			response.put("key", "200");
			response.put("userId", alluser.getUserId());
			response.put("userRole", alluser.getUserRolesPl().getUserRoleName());
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");

		}
		return response;
	}

	public HashMap<String, Object> getEventSetsForCustomer(int customerId) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			List<AllEventSet> customerEventSets = drCustomerRepo.getEventSetsForCustomer(customerId);
			List<EventSetDetailsDto> eventSetDetailsDtoList = new ArrayList<EventSetDetailsDto>();
			for (int i = 0; i < customerEventSets.size(); i++) {
				EventSetDetailsDto eventSetDetailsDto = new EventSetDetailsDto();
				eventSetDetailsDto.setEventSetId(customerEventSets.get(i).getEventSetId());
				eventSetDetailsDto.setCreatedTs(customerEventSets.get(i).getCreatedTs());
				eventSetDetailsDto.setEventSetStatus(customerEventSets.get(i).getEventSetStatusPl().getStatusName());
				eventSetDetailsDto.setEventSetName(customerEventSets.get(i).getName());
				List<EventSetEventDto> listOfEvents = new ArrayList<EventSetEventDto>();

				List<AllEvent> eventsForCustomer = drCustomerRepo.getEventsForCustomerAndEventSet(customerId,
						customerEventSets.get(i).getEventSetId());

				for (int j = 0; j < eventsForCustomer.size(); j++) {
					EventSetEventDto eventSetEvent = new EventSetEventDto();
					eventSetEvent.setEventId(eventsForCustomer.get(j).getEventId());
					eventSetEvent.setPlannedPower(String.valueOf(eventsForCustomer.get(j).getPlannedPower()));
					eventSetEvent.setPlannedPrice(String.valueOf(eventsForCustomer.get(j).getExpectedPrice()));
					EventCustomerMapping ecm = drCustomerRepo.getEventCustomerMapping(customerId,
							eventsForCustomer.get(j).getEventId());
					eventSetEvent.setEventCustomerMappingStatus(String.valueOf(ecm.getEventCustomerStatusId()));

					listOfEvents.add(eventSetEvent);

				}
				eventSetDetailsDto.setEvents(listOfEvents);
				eventSetDetailsDtoList.add(eventSetDetailsDto);
			}

			response.put("eventSets", eventSetDetailsDtoList);
			response.put("message", CustomMessages.getCustomMessages("AS"));
			response.put("key", "200");
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");

		}
		return response;
	}

	public HashMap<String, Object> getEventsForCustomerAndEventSet(int customerId, int eventSetId) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			List<AllEvent> cusomterEvents = drCustomerRepo.getEventsForCustomerAndEventSet(customerId, eventSetId);
			List<CustomerEventDetailsDto> allEvents = new ArrayList<CustomerEventDetailsDto>();
			for (int i = 0; i < cusomterEvents.size(); i++) {
				CustomerEventDetailsDto customerEventDetails = new CustomerEventDetailsDto();
				customerEventDetails.setEventId(cusomterEvents.get(i).getEventId());
				customerEventDetails.setEventStatus(cusomterEvents.get(i).getEventStatusPl().getName());
				customerEventDetails.setEventName(cusomterEvents.get(i).getEventName());
				customerEventDetails.setEventStartTime(cusomterEvents.get(i).getEventStartTime());
				customerEventDetails.setEventEndTime(cusomterEvents.get(i).getEventEndTime());
				customerEventDetails.setPlannedPower(cusomterEvents.get(i).getPlannedPower());
				customerEventDetails.setCommitedPower(cusomterEvents.get(i).getCommitedPower());
				customerEventDetails.setActualPower(cusomterEvents.get(i).getActualPower());
				customerEventDetails.setExpectedPrice(cusomterEvents.get(i).getExpectedPrice());
				customerEventDetails.setCreatedTs(cusomterEvents.get(i).getCreatedTs());
				// customerEventDetails.setAllCustomerDevices(drCustomerRepo.getAllUserDevices(customerId));
				EventCustomerMapping ecm = drCustomerRepo.getEventCustomerMapping(customerId,
						cusomterEvents.get(i).getEventId());
				CustomerEventMappingDetailsDto cem = new CustomerEventMappingDetailsDto();
				cem.setEventCustomerMappingId(ecm.getEventCustomerMappingId());
				cem.setBidTs(ecm.getBidTs());
				cem.setBidPrice(ecm.getBidPrice());
				cem.setCommittedPower(ecm.getCommitedPower());
				cem.setActualPower(ecm.getActualPower());
				cem.setEventCustomerStatus(ecm.getEventCustomerStatusId());
				cem.setCounterBidFlag(ecm.getCounterBidFlag());
				cem.setCounterBidAmount(ecm.getCounterBidAmount());
				cem.setCreatedTs(ecm.getCreatedTs());
				List<UserDRDevices> udd = drCustomerRepo.getuserMappedDevices(ecm.getEventCustomerMappingId());
				
				List<DRDeviceDto> mappedDevices = new ArrayList<DRDeviceDto>();
				for (int j = 0; j < udd.size(); j++) {
					DRDeviceDto mappedDevice = new DRDeviceDto();
					mappedDevice.setDeviceCapacity(udd.get(j).getDevice_capacity());
					mappedDevice.setDrDeviceId(udd.get(j).getUserDrDeviceId());
					mappedDevice.setDrDeviceName(udd.get(j).getDeviceName());
					mappedDevices.add(mappedDevice);
				}
				cem.setMappedDevices(mappedDevices);
				customerEventDetails.setEventCustomerDetails(cem);
				allEvents.add(customerEventDetails);
			}
			List<UserDRDevices> aud = drCustomerRepo.getAllUserDevices(customerId);
			List<DRDeviceDto> userDRdevices = new ArrayList<DRDeviceDto>();

			for (int i = 0; i < aud.size(); i++) {
				DRDeviceDto ddd = new DRDeviceDto();
				ddd.setDeviceCapacity(aud.get(i).getDevice_capacity());
				ddd.setDrDeviceId(aud.get(i).getUserDrDeviceId());
				ddd.setDrDeviceName(aud.get(i).getDeviceName());
				userDRdevices.add(ddd);
			}
			response.put("events", allEvents);
			response.put("allCustomerDevices", userDRdevices);
			response.put("message", CustomMessages.getCustomMessages("AS"));
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
