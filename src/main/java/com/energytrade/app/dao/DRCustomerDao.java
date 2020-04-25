package com.energytrade.app.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.DevicePl;
import com.energytrade.app.model.EventCustomerMapping;
import com.energytrade.app.model.EventSetEventDto;
import com.energytrade.app.model.UserAccessTypeMapping;
import com.energytrade.app.model.UserDRDevices;
import com.energytrade.app.model.UserDevice;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.model.UserTypePl;
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
	
	@Autowired
	AllUserRepository alluserrepo;
	
	@Autowired
	UserAccessRepository useraccessrepo;
	
	@Autowired
	UserDRDevicesRepository userdrdevicerepo;

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
			UserTypePl userType = alluserrepo.getUserType("DR");
			UserAccessTypeMapping ustmp = new UserAccessTypeMapping();
			ustmp.setAllUser(alluser);
			ustmp.setUserTypepl(userType);
			useraccessrepo.saveAndFlush(ustmp);
			
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
	
	public HashMap<String, Object> getDRCustomerProfile(int customerId) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			AllUser alluser = alluserrepo.getUserById(customerId);
			HashMap<String,Object> userDetails = new HashMap<>();
			response.put("fullName", alluser.getFullName());
			response.put("phoneNumber", alluser.getPhoneNumber());
			response.put("email", alluser.getEmail());
			if (alluser.getRegistrationDate() != null) {
				response.put("registrationDate", alluser.getRegistrationDate());
			} else {
				response.put("registrationDate", null);
			}
			response.put("activeStatus", alluser.getActiveStatus());
			List<HashMap<String,String>> listOfAccessLevels = new ArrayList<>();
			 if (alluser.getUserAccessMap() != null) {
				   for (int i=0;i<alluser.getUserAccessMap().size();i++) {
					   HashMap<String,String> userAccessLevel = new HashMap<>();
					   userAccessLevel.put("accessLevel", alluser.getUserAccessMap().get(i).getUserTypepl().getUserTypeName());
					   listOfAccessLevels.add(userAccessLevel);
					   
				   }
			 }	   
			 response.put("userRole", listOfAccessLevels);
			   if (alluser.getDrContractNumber() != null) {
				   response.put("drContractNumber", alluser.getDrContractNumber());
				} else {
					response.put("drContractNumber", null);
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
				response.put("drCustomerDevice", userDRdevices);

			 response.put("drContractDetails", drCustomerRepo.getBusinessContractDetails(alluser.getDrContractNumber()));
			//response.put("eventSets", eventSetDetailsDtoList);
			response.put("message", CustomMessages.getCustomMessages("FS"));
			response.put("key", "200");
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");

		}
		return response;
	}
	
	public HashMap<String, Object> updateDRCustomerDevice(HashMap<String,Object> inputDetails) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			drCustomerRepo.updateDrDeviceDetails((String)inputDetails.get("deviceName"),Double.parseDouble((String)inputDetails.get("deviceCapacity")) ,(int) inputDetails.get("userDrDeviceId"));	
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
	
	public HashMap<String, Object> deleteDrDevice(int userDrDeviceId) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			drCustomerRepo.deleteEventCustomerDevices(userDrDeviceId);
			drCustomerRepo.deleteDrDeviceDetails(userDrDeviceId);
			response.put("message", CustomMessages.getCustomMessages("DS"));
			response.put("key", "200");
		} catch (Exception e) {
			System.out.println("Error in checkExistence" + e.getMessage());
			e.printStackTrace();
			response.put("message", CustomMessages.getCustomMessages("ISE"));
			response.put("key", "500");

		}
		return response;
	}

    public HashMap<String,Object> addDrDevice(HashMap<String,Object> deviceDetails) {
        
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	try {
        	AllUser alluser=alluserrepo.getUserById((int)(deviceDetails.get("userId")));
        	List<HashMap<String,String>> listOfUserDevice=(ArrayList<HashMap<String,String>>)deviceDetails.get("deviceDetails");
        	int count=userdrdevicerepo.getDrDeviceCount();
        	ArrayList<UserDRDevices> listofDevices = new ArrayList<UserDRDevices>();
        	for(int i=0;i<listOfUserDevice.size();i++) {
        		HashMap<String,String> drdeviceDetails = listOfUserDevice.get(i);
        		count= count+1;
        		UserDRDevices userdrdevices = new UserDRDevices();
        		userdrdevices.setAllUser(alluser);
        		userdrdevices.setUserDrDeviceId(count);
        		userdrdevices.setDeviceName(drdeviceDetails.get("deviceName"));
        		userdrdevices.setDevice_capacity(Double.parseDouble(drdeviceDetails.get("deviceCapacity")));
        		listofDevices.add(userdrdevices);
        		
        	}
        	userdrdevicerepo.saveAll(listofDevices);
        	response.put("message",CustomMessages.getCustomMessages("SUC"));
      	   response.put("key","200");
        	
        }
        catch (Exception e) {
            System.out.println("Error in checkExistence" + e.getMessage());
            e.printStackTrace();
            response.put("message",CustomMessages.getCustomMessages("ISE"));
     	   response.put("key","500");
           
        }
        return response;
    }
    

}

