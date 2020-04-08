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
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.util.CustomMessages;
import com.energytrade.app.dto.AllEventSetDto;
import com.energytrade.app.dto.EventSetDetailsDto;
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
				for (int j = 0; j < drCustomerRepo
						.getEventsForCustomerAndEventSet(customerId, customerEventSets.get(i).getEventSetId())
						.size(); j++) {
					EventSetEventDto eventSetEvent = new EventSetEventDto();
					eventSetEvent.setEventId(customerEventSets.get(i).getAllEvents().get(j).getEventId());
					eventSetEvent.setPlannedPower(
							String.valueOf(customerEventSets.get(i).getAllEvents().get(j).getPlannedPower()));
					eventSetEvent.setPlannedPrice(
							String.valueOf(customerEventSets.get(i).getAllEvents().get(j).getExpectedPrice()));
					EventCustomerMapping ecm = drCustomerRepo.getEventCustomerMapping(customerId,
							customerEventSets.get(i).getAllEvents().get(j).getEventId());
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
}
