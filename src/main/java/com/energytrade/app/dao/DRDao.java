package com.energytrade.app.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.energytrade.app.dto.AllEventDto;
import com.energytrade.app.dto.AllEventSetDto;
import com.energytrade.app.dto.EventCustomerDto;
import com.energytrade.app.model.AllContract;
import com.energytrade.app.model.AllEvent;
import com.energytrade.app.model.AllEventSet;
import com.energytrade.app.model.AllForecast;
import com.energytrade.app.model.AllOtp;
import com.energytrade.app.model.AllSellOrder;
import com.energytrade.app.model.AllTimeslot;
import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.ContractStatusPl;
import com.energytrade.app.model.DevicePl;
import com.energytrade.app.model.EventCustomerMapping;
import com.energytrade.app.model.EventCustomerStatusPl;
import com.energytrade.app.model.EventSetStatusPl;
import com.energytrade.app.model.EventStatusPl;
import com.energytrade.app.model.NonTradehourStatusPl;
import com.energytrade.app.model.NotificationRequestDto;
import com.energytrade.app.model.OrderStatusPl;
import com.energytrade.app.model.UserAccessLevelMapping;
import com.energytrade.app.model.UserDevice;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.util.ApplicationConstant;
import com.energytrade.app.util.CommonUtility;
import com.energytrade.app.util.CompareHelper;
import com.energytrade.app.util.CustomMessages;
import com.energytrade.app.util.PushHelper;
import com.energytrade.app.util.ValidationUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Transactional
@Repository
public class DRDao extends AbstractBaseDao {
	@Autowired
	EventSetRepository eventsetrepo;

	@Autowired
	EventRepository eventrepo;
	
	@Autowired
    AllUserRepository alluserrepo;
	
	@Autowired
    EventCustomerRepository eventcustomerrepo;


	public HashMap<String,Object> createEventSet(String filePath, byte[] imageByte, String location, int userId) {
		
		HashMap<String,Object> response= new HashMap<String, Object>();
		HashMap<String,Object> internalresponse= new HashMap<String, Object>();
		try {
			ArrayList<Object> eventSetObjects = createEventSet(location, userId);
			AllEventSetDto allEventSets= (AllEventSetDto)eventSetObjects.get(0);
			//allEventSets.set
			// internalresponse.put("eventSet", eventSetObjects.get(0));
			List<AllEventDto> listOfEvents = createFile(filePath, imageByte, eventSetObjects.get(1));
			allEventSets.setAllEvents(listOfEvents);
			ArrayList<String> powerAndPrice = getPower(listOfEvents);
			allEventSets.setPlannedPower(powerAndPrice.get(0));
			allEventSets.setTotlaPrice(powerAndPrice.get(1));
			allEventSets.setActualPower("0");
			allEventSets.setCancelledEvents("0");
			allEventSets.setCompletedEvents("0");
			allEventSets.setPublishedEvents("0");
			eventsetrepo.updateEventSet(Double.parseDouble(powerAndPrice.get(0)), Double.parseDouble(powerAndPrice.get(1)), allEventSets.getEventSetId());
			//internalresponse.put("events", listOfEvents);
			 internalresponse.put("eventSet", allEventSets);
			 
			response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("response", internalresponse);
			response.put("customMessage", null);
			// createEventSetObjects(file);
			// Create Workbook instance holding reference to .xlsx file

		} catch (Exception e) {
			e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("customMessage", null);
			response.put("response", null);
		} finally {

		}
		return response;
	}

	public ArrayList<Object> createEventSet(String location, int userId) {
		AllEventSet alleventset1 = new AllEventSet();
		AllEventSetDto alleventsetdto = new AllEventSetDto();
		ArrayList<Object> listOfObjects = new ArrayList<Object>();
		try {
			int count = eventsetrepo.getEventSetCount()+1;
			Date d = new Date();
			int year = d.getYear();
			int month = d.getMonth();
			int date = d.getDate();
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			AllUser alluser = eventsetrepo.getUserById(userId);
			AllEventSet alleventset = new AllEventSet();
			
			EventSetStatusPl eventsetstatuspl = eventsetrepo.getEventSetStatus("Created");
			alleventset.setName(location + year + month + date);
			alleventset.setAllUser(alluser);
			alleventset.setEventSetId(count);
			alleventset.setEventSetStatusPl(eventsetstatuspl);
			alleventset.setUploadTime(ts);
			alleventset.setDate(new Date());
			alleventsetdto.setEventSetName(location + year + month + date);
			alleventsetdto.setUserId(alluser.getUserId());
			alleventsetdto.setUserName(alluser.getFullName());
			alleventsetdto.setEventSetId(count);
			alleventsetdto.setEventSetStatus(eventsetstatuspl.getStatusName());
			alleventsetdto.setDateOfOccurence(ts.toString());
			eventsetrepo.saveAndFlush(alleventset);
			alleventset1 = eventsetrepo.getEventSet(count);
			alleventsetdto.setDateOfOccurence(ts.toString());
			listOfObjects.add(alleventsetdto);
			listOfObjects.add(alleventset1);
			// return alleventset1;
		} catch (Exception e) {

		} finally {

		}
		return listOfObjects;

	}

	public List<EventCustomerDto> createEventCustomer(AllEvent event) {
		ArrayList<Object> listOfObjects = new ArrayList<Object>();
		List<EventCustomerMapping> listOfEventCustMapping = new ArrayList<EventCustomerMapping>();
		List<EventCustomerDto> listOfEventCustDto = new ArrayList<EventCustomerDto>();
		try {
			int count = eventcustomerrepo.getEventCustomerCount();
			List<UserAccessLevelMapping> listOfusers = eventcustomerrepo.getUserAccessLevel(event.getAllEventSet().getAllUser().getUserId());
			//	EventCustomerStatusPl eventstatus = eventcustomerrepo.getEventCustomerStatus(1);
				for (int j=0;j<listOfusers.size();j++) {
					count++;
					EventCustomerDto eventCustomerDto = new EventCustomerDto();
					EventCustomerMapping eventCustomerMapping = new EventCustomerMapping();
					eventCustomerMapping.setAllUser(listOfusers.get(j).getAllUser());
					eventCustomerMapping.setEventCustomerMappingId(count);
					eventCustomerMapping.setAllEvent(event);
					eventCustomerMapping.setCounterBidAmount(0);
					eventCustomerMapping.setCommitedPower(0);
					eventCustomerMapping.setActualPower(0);
					eventCustomerMapping.setCounterBidFlag("N");
					eventCustomerMapping.setBidPrice(event.getExpectedPrice());
					eventCustomerDto.setUserId(listOfusers.get(j).getAllUser().getUserId());
					eventCustomerDto.setUserName(listOfusers.get(j).getAllUser().getFullName());
					eventCustomerDto.setEventId(event.getEventId());
					eventCustomerDto.setIsSelected("Y");
					eventCustomerMapping.setEventCustomerStatusId(1);
					// eventCustomerDto.setActualPower(allevent.getActualPower());
					eventCustomerDto.setParticipationStatus("1");
					listOfEventCustDto.add(eventCustomerDto);
					listOfEventCustMapping.add(eventCustomerMapping);
			}
			eventcustomerrepo.saveAll(listOfEventCustMapping);
			// return alleventset1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return listOfEventCustDto;

	}

	public List<AllEventDto> createFile(String filePath, byte[] imageByte, Object eventSet) throws IOException {

		FileInputStream file = null;
		List<AllEventDto> listOfEvents = new ArrayList<AllEventDto>();
		try {
			new FileOutputStream(filePath).write(imageByte);
			file = new FileInputStream(new File(filePath));
			listOfEvents=createEventSetObjects(file, eventSet);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			file.close();
		}
		return listOfEvents;
	}

	public List<AllEventDto> createEventSetObjects(FileInputStream file, Object alleventset1) throws IOException {

		List<AllEventDto> listOfEventsDto = new ArrayList<AllEventDto>();
		List<AllEvent> listOfEvents = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			CommonUtility cm = new CommonUtility();
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);
			AllEventSet alleventset =(AllEventSet)alleventset1;
			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			Row firstrow = rowIterator.next();
			int count = 0;
			int rowCount = eventrepo.getEventCount();
			ArrayList<Date> listOfDates = new ArrayList<Date>();
			EventStatusPl eventstatuspl= eventrepo.getEventStatus("Created");
			//List<AllEvent> listOfEvents = new ArrayList<AllEvent>();
			while (rowIterator.hasNext()) {
				count++;
				rowCount++;
				AllEvent allevent = new AllEvent();
				AllEventDto alleventsetdto = new AllEventDto();
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
//                 Cell cell1= row.getCell(0);
//                 System.out.println(cell1.getStringCellValue());
				Cell cell2 = row.getCell(1);
				System.out.println(cell2.getStringCellValue());
				Cell cell3 = row.getCell(2);
				System.out.println(cell3.getNumericCellValue());
				Cell cell4 = row.getCell(3);
				if (cell4 != null) {
					System.out.println(cell4.getNumericCellValue());
				}
				alleventsetdto.setEventId(rowCount);
				alleventsetdto.setEventName(alleventset.getName() + count);
				alleventsetdto.setEventSetId(alleventset.getEventSetId());
				alleventsetdto.setEventStatus(eventstatuspl.getName());
				
				allevent.setEventId(rowCount);
				allevent.setEventName(alleventset.getName() + count);
				allevent.setAllEventSet(alleventset);
				allevent.setEventStatusPl(eventstatuspl);
				allevent.setPlannedPower(cell3.getNumericCellValue());
				alleventsetdto.setPlannedPower(Double.toString(cell3.getNumericCellValue()));
				if (cell4 != null) {
					allevent.setExpectedPrice(cell4.getNumericCellValue());
					alleventsetdto.setPrice(Double.toString(cell4.getNumericCellValue()));
				} else {
					allevent.setExpectedPrice(0);
					alleventsetdto.setPrice("0");
				}
				listOfDates=cm.getDateFormatted(cell2.getStringCellValue());
				allevent.setEventStartTime(listOfDates.get(0));
				allevent.setEventEndTime(listOfDates.get(1));
				alleventsetdto.setActualPower(null);
				alleventsetdto.setCommittedPower(null);
				alleventsetdto.setCounterBidCustomers(0);
				alleventsetdto.setInvitedCustomers(0);
				alleventsetdto.setNoResponseCustomers(0);
				alleventsetdto.setParticipatedCustomers(0);
				alleventsetdto.setEndTime(listOfDates.get(1).toString());
				alleventsetdto.setStartTime(listOfDates.get(0).toString());
				alleventsetdto.setParticipatedCustomers(0);
				alleventsetdto.setShortfall(null);
				allevent.setActualPower(0);
				// allevent.setCommittedPower(0);
				allevent.setCounterBidCustomers("0");
				allevent.setInvitedCustomers(0);
				allevent.setNoResponseCustomers(0);
				allevent.setParticipatedCustomers(0);
				allevent.setParticipatedCustomers(0);
				alleventsetdto.setShortfall("0");
				eventrepo.saveAndFlush(allevent);
				List<EventCustomerDto> listOfCustomers= createEventCustomer(allevent);
				alleventsetdto.setListOfCustomers(listOfCustomers);
				alleventsetdto.setNumberOfCustomers(Integer.toString(listOfCustomers.size()));
				System.out.println("");
				listOfEvents.add(allevent);
				listOfEventsDto.add(alleventsetdto);
			}
//			eventrepo.saveAll(listOfEvents);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			file.close();
		}
		return listOfEventsDto;
	}

	public HashMap<String, Object> loginUser(String phoneNumber, String password) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		try {

			int count = eventsetrepo.loginUser(phoneNumber, password);
			if (count > 0) {
				response.put("status", "LOGGED_IN");
			}else {
				response.put("status", "WRONG_CREDENTIALS");
			}

		} catch (Exception e) {

		} finally {

		}
		return response;
	}

	public HashMap<String, Object> getEventDetails(List<Integer> eventId) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			
			List<AllEventDto> listOfEventsdto= new ArrayList<AllEventDto>();
			for (int i=0;i<eventId.size();i++) {
			AllEvent allevent = eventrepo.getEventById(eventId.get(i));
			AllEventDto alleventdto = new AllEventDto();
			alleventdto.setEventId(allevent.getEventId());
			alleventdto.setEventName(allevent.getEventName());
			alleventdto.setEventSetId(allevent.getAllEventSet().getEventSetId());
			alleventdto.setEventStatus(allevent.getEventStatusPl().getName());
			alleventdto.setPower(allevent.getActualPower());
			alleventdto.setPlannedPower(Double.toString(allevent.getPlannedPower()));
			alleventdto.setPrice(Double.toString(allevent.getExpectedPrice()));
			alleventdto.setNumberOfCustomers(Integer.toString(allevent.getEventCustomerMappings().size()));
			alleventdto.setStartTime(allevent.getEventStartTime().toString());
			alleventdto.setEndTime(allevent.getEventEndTime().toString());
			alleventdto.setCommittedPower(Double.toString(allevent.getCommitedPower()));
			alleventdto.setShortfall("0");
			alleventdto.setActualPower(Double.toString(allevent.getActualPower()));
			alleventdto.setStartTime(allevent.getEventStartTime().toString());
			List<EventCustomerDto> listOfCustomers= new ArrayList<EventCustomerDto>();
			
			for (int j=0;j<allevent.getEventCustomerMappings().size();j++) {
				EventCustomerDto evdto= new EventCustomerDto();
				evdto.setEventId(allevent.getEventId());
				evdto.setUserId(allevent.getEventCustomerMappings().get(j).getAllUser().getUserId());
				evdto.setUserName(allevent.getEventCustomerMappings().get(j).getAllUser().getFullName());
				evdto.setActualPower(allevent.getActualPower());
				evdto.setCommitments(allevent.getCommitedPower());
				evdto.setPrice(allevent.getEventCustomerMappings().get(j).getBidPrice());
				evdto.setIsSelected("Y");
				// eventCustomerDto.setActualPower(allevent.getActualPower());
				evdto.setParticipationStatus("1");
				listOfCustomers.add(evdto);
			}
			alleventdto.setListOfCustomers(listOfCustomers);
			listOfEventsdto.add(alleventdto);
			}
			response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("response", listOfEventsdto);
			response.put("customMessage", null);
			// createEventSetObjects(file);
			// Create Workbook instance holding reference to .xlsx file

		} catch (Exception e) {
			e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("customMessage", null);
			response.put("response", null);
		} finally {

		}
		return response;
	}

	public HashMap<String, Object> getEventSetDetails(int eventSetId) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> internalresponse = new HashMap<String, Object>();
		List<AllEventDto> listOfEvents= new ArrayList<AllEventDto>();
		try {
			
			AllEventSet alleventset = eventsetrepo.getEventSet(eventSetId);
			AllEventSetDto alleventsetdto = new AllEventSetDto();
			alleventsetdto.setEventSetId(alleventset.getEventSetId());
			alleventsetdto.setEventSetName(alleventset.getName());
			alleventsetdto.setEventSetStatus(alleventset.getEventSetStatusPl().getStatusName());
			alleventsetdto.setUserId(alleventset.getAllUser().getUserId());
			alleventsetdto.setUserName(alleventset.getAllUser().getFullName());
			for (int i=0;i<alleventset.getAllEvents().size();i++) {
				AllEventDto alleventdto = new AllEventDto();
				alleventdto.setEventId(alleventset.getAllEvents().get(i).getEventId());
				alleventdto.setEventName(alleventset.getAllEvents().get(i).getEventName());
				alleventdto.setEventStatus(alleventset.getAllEvents().get(i).getEventStatusPl().getName());
				alleventdto.setPower(alleventset.getAllEvents().get(i).getActualPower());
				listOfEvents.add(alleventdto);
			}
			
			internalresponse.put("eventSetDetails", alleventsetdto);
			internalresponse.put("events",listOfEvents);
			response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("response", internalresponse);
			response.put("customMessage", null);

		} catch (Exception e) {
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error");
			response.put("response", null);
			response.put("customMessage", null);
		} finally {

		}
		return response;
	}
	public HashMap<String,Object> loginDSOUser(String email,String password) throws ParseException {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,-7);
		Date weekdate = cal.getTime();
		cal.setTime(date);
		cal.add(Calendar.DATE,-30);
		Date monthdate = cal.getTime();
	    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	    String weekdates = sdf.format(weekdate);
	    String monthdates = sdf.format(monthdate);
	    String dates = sdf.format(date);
	    weekdate = sdf.parse(weekdates);
	    monthdate = sdf.parse(monthdates);
	    date = sdf.parse(dates);
		HashMap<String,Object> response=new HashMap<String, Object>();
		HashMap<String,Object> internalresponse=new HashMap<String, Object>();
		CompareHelper ch= new CompareHelper();
	    try {
	   int count=this.alluserrepo.loginDSOUser(email, password);
	   ArrayList<String> status = new ArrayList<String>();
	   if(count >= 1) {
		   AllUser alluser= alluserrepo.getUserBymail(email);
		   List<AllEventSet> listOfWeeklyEvents = eventrepo.getEventSetBydate(weekdate,date);
		   List<AllEventSet> listOfMonthlyEvents = eventrepo.getEventSetBydate(monthdate,date);
		   List<AllEventSet> listOfUpcomingEvents = eventrepo.getupcomingEventSet(date);
		   List<AllEventSetDto> listOfWeeklyEventSetDto = new ArrayList<AllEventSetDto>();
		   List<AllEventSetDto> listOfMonthlyEventSetDto = new ArrayList<AllEventSetDto>();
		   List<AllEventSetDto> listOfUpcomingEventSetDto = new ArrayList<AllEventSetDto>();
		  
		   for(int i =0;i<listOfWeeklyEvents.size();i++) {
			   status=ch.countdata(listOfWeeklyEvents.get(i).getAllEvents());
			   AllEventSetDto alleventsetdto = new AllEventSetDto();
			   alleventsetdto.setEventSetName(listOfWeeklyEvents.get(i).getName());
			   alleventsetdto.setEventSetId(listOfWeeklyEvents.get(i).getEventSetId());
			   alleventsetdto.setEventSetStatus(listOfWeeklyEvents.get(i).getEventSetStatusPl().getStatusName());
			   alleventsetdto.setUserId(listOfWeeklyEvents.get(i).getAllUser().getUserId());
			   alleventsetdto.setDateOfOccurence(listOfWeeklyEvents.get(i).getUploadTime().toString());
			   alleventsetdto.setTotlaPrice(Double.toString(listOfWeeklyEvents.get(i).getTotalPrice()));
			   alleventsetdto.setPlannedPower(Double.toString(listOfWeeklyEvents.get(i).getPlannedPower()));
			   alleventsetdto.setUserName(listOfWeeklyEvents.get(i).getAllUser().getFullName());
			   alleventsetdto.setActualPower(Double.toString(listOfWeeklyEvents.get(i).getActualPower()));
			   alleventsetdto.setPublishedEvents(status.get(0));
			   alleventsetdto.setCompletedEvents(status.get(1));
			   alleventsetdto.setCancelledEvents(status.get(2));
			   List<AllEventDto> listOfEvents= new ArrayList<AllEventDto>();
			   for (int j=0;j<listOfWeeklyEvents.get(i).getAllEvents().size();j++) {
					AllEventDto alleventdto = new AllEventDto();
					alleventdto.setEventId(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventId());
					alleventdto.setEventName(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventName());
					alleventdto.setEventStatus(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventStatusPl().getName());
					alleventdto.setPower(listOfWeeklyEvents.get(i).getAllEvents().get(j).getActualPower());
					alleventdto.setPlannedPower(Double.toString(listOfWeeklyEvents.get(i).getAllEvents().get(j).getPlannedPower()));
					alleventdto.setPrice(Double.toString(listOfWeeklyEvents.get(i).getAllEvents().get(j).getExpectedPrice()));
					alleventdto.setNumberOfCustomers(Integer.toString(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventCustomerMappings().size()));
					alleventdto.setStartTime(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventStartTime().toString());
					alleventdto.setEndTime(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventEndTime().toString());
					alleventdto.setCommittedPower(Double.toString(listOfWeeklyEvents.get(i).getAllEvents().get(j).getCommitedPower()));
					alleventdto.setShortfall("0");
					alleventdto.setActualPower(Double.toString(listOfWeeklyEvents.get(i).getAllEvents().get(j).getActualPower()));
					alleventdto.setStartTime(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventStartTime().toString());
					List<EventCustomerMapping> listOfCustomers = eventcustomerrepo.getEventCustomerMappings(listOfWeeklyEvents.get(i).getAllEvents().get(j).getEventId());
					List<EventCustomerDto> listOfCustomersdto = new ArrayList<EventCustomerDto>();
					for(int k=0;k<listOfCustomers.size();k++) {
						EventCustomerDto eventcustomerdto = new EventCustomerDto();
						eventcustomerdto.setUserId(listOfCustomers.get(k).getAllUser().getUserId());
						eventcustomerdto.setUserName(listOfCustomers.get(k).getAllUser().getFullName());
						eventcustomerdto.setActualPower(listOfCustomers.get(k).getActualPower());
						eventcustomerdto.setCommitments(listOfCustomers.get(k).getCommitedPower());
						eventcustomerdto.setPrice(listOfCustomers.get(k).getBidPrice());
						eventcustomerdto.setIsSelected("Y");
						// eventCustomerDto.setActualPower(allevent.getActualPower());
						eventcustomerdto.setParticipationStatus("1");
						listOfCustomersdto.add(eventcustomerdto);
						
					}
					alleventdto.setListOfCustomers(listOfCustomersdto);
					listOfEvents.add(alleventdto);
				}
			   alleventsetdto.setAllEvents(listOfEvents);
			   listOfWeeklyEventSetDto.add(alleventsetdto);
		   }
		   
		   for(int i =0;i<listOfMonthlyEvents.size();i++) {
			   status=ch.countdata(listOfMonthlyEvents.get(i).getAllEvents());
			   AllEventSetDto alleventsetdto = new AllEventSetDto();
			   alleventsetdto.setEventSetName(listOfMonthlyEvents.get(i).getName());
			   alleventsetdto.setEventSetId(listOfMonthlyEvents.get(i).getEventSetId());
			   alleventsetdto.setEventSetStatus(listOfMonthlyEvents.get(i).getEventSetStatusPl().getStatusName());
			   alleventsetdto.setUserId(listOfMonthlyEvents.get(i).getAllUser().getUserId());
			   alleventsetdto.setUserName(listOfMonthlyEvents.get(i).getAllUser().getFullName());
			   alleventsetdto.setDateOfOccurence(listOfMonthlyEvents.get(i).getUploadTime().toString());
			   alleventsetdto.setTotlaPrice(Double.toString(listOfMonthlyEvents.get(i).getTotalPrice()));
			   alleventsetdto.setPlannedPower(Double.toString(listOfMonthlyEvents.get(i).getPlannedPower()));
			   alleventsetdto.setActualPower(Double.toString(listOfMonthlyEvents.get(i).getActualPower()));
			   alleventsetdto.setPublishedEvents(status.get(0));
			   alleventsetdto.setCompletedEvents(status.get(1));
			   alleventsetdto.setCancelledEvents(status.get(2));
			   List<AllEventDto> listOfEvents= new ArrayList<AllEventDto>();
			   for (int j=0;j<listOfMonthlyEvents.get(i).getAllEvents().size();j++) {
					AllEventDto alleventdto = new AllEventDto();
					alleventdto.setEventId(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventId());
					alleventdto.setEventName(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventName());
					alleventdto.setEventStatus(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventStatusPl().getName());
					alleventdto.setPower(listOfMonthlyEvents.get(i).getAllEvents().get(j).getActualPower());
					alleventdto.setPlannedPower(Double.toString(listOfMonthlyEvents.get(i).getAllEvents().get(j).getPlannedPower()));
					alleventdto.setPrice(Double.toString(listOfMonthlyEvents.get(i).getAllEvents().get(j).getExpectedPrice()));
					alleventdto.setNumberOfCustomers(Integer.toString(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventCustomerMappings().size()));
					alleventdto.setStartTime(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventStartTime().toString());
					alleventdto.setEndTime(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventEndTime().toString());
					alleventdto.setCommittedPower(Double.toString(listOfMonthlyEvents.get(i).getAllEvents().get(j).getCommitedPower()));
					alleventdto.setShortfall("0");
					alleventdto.setActualPower(Double.toString(listOfMonthlyEvents.get(i).getAllEvents().get(j).getActualPower()));
					alleventdto.setStartTime(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventStartTime().toString());
					List<EventCustomerMapping> listOfCustomers = eventcustomerrepo.getEventCustomerMappings(listOfMonthlyEvents.get(i).getAllEvents().get(j).getEventId());
					List<EventCustomerDto> listOfCustomersdto = new ArrayList<EventCustomerDto>();
					for(int k=0;k<listOfCustomers.size();k++) {
						EventCustomerDto eventcustomerdto = new EventCustomerDto();
						eventcustomerdto.setUserId(listOfCustomers.get(k).getAllUser().getUserId());
						eventcustomerdto.setUserName(listOfCustomers.get(k).getAllUser().getFullName());
						eventcustomerdto.setActualPower(listOfCustomers.get(k).getActualPower());
						eventcustomerdto.setCommitments(listOfCustomers.get(k).getCommitedPower());
						eventcustomerdto.setPrice(listOfCustomers.get(k).getBidPrice());
						eventcustomerdto.setIsSelected("Y");
						// eventCustomerDto.setActualPower(allevent.getActualPower());
						eventcustomerdto.setParticipationStatus("1");
						listOfCustomersdto.add(eventcustomerdto);
						
					}
					alleventdto.setListOfCustomers(listOfCustomersdto);
					listOfEvents.add(alleventdto);
				}
			   alleventsetdto.setAllEvents(listOfEvents);
			   listOfMonthlyEventSetDto.add(alleventsetdto);
		   }
		   
		   for(int i =0;i<listOfUpcomingEvents.size();i++) {
			   status=ch.countdata(listOfUpcomingEvents.get(i).getAllEvents());
			   AllEventSetDto alleventsetdto = new AllEventSetDto();
			   alleventsetdto.setEventSetName(listOfUpcomingEvents.get(i).getName());
			   alleventsetdto.setEventSetId(listOfUpcomingEvents.get(i).getEventSetId());
			   alleventsetdto.setEventSetStatus(listOfUpcomingEvents.get(i).getEventSetStatusPl().getStatusName());
			   alleventsetdto.setUserId(listOfUpcomingEvents.get(i).getAllUser().getUserId());
			   alleventsetdto.setUserName(listOfUpcomingEvents.get(i).getAllUser().getFullName());
			   alleventsetdto.setDateOfOccurence(listOfUpcomingEvents.get(i).getUploadTime().toString());
			   alleventsetdto.setTotlaPrice(Double.toString(listOfUpcomingEvents.get(i).getTotalPrice()));
			   alleventsetdto.setPlannedPower(Double.toString(listOfUpcomingEvents.get(i).getPlannedPower()));
			   alleventsetdto.setActualPower(Double.toString(listOfUpcomingEvents.get(i).getActualPower()));
			   alleventsetdto.setPublishedEvents(status.get(0));
			   alleventsetdto.setCompletedEvents(status.get(1));
			   alleventsetdto.setCancelledEvents(status.get(2));
			   List<AllEventDto> listOfEvents= new ArrayList<AllEventDto>();
			   for (int j=0;j<listOfUpcomingEvents.get(i).getAllEvents().size();j++) {
					AllEventDto alleventdto = new AllEventDto();
					alleventdto.setEventId(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventId());
					alleventdto.setEventName(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventName());
					alleventdto.setEventStatus(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventStatusPl().getName());
					alleventdto.setPower(listOfUpcomingEvents.get(i).getAllEvents().get(j).getActualPower());
					alleventdto.setPlannedPower(Double.toString(listOfUpcomingEvents.get(i).getAllEvents().get(j).getPlannedPower()));
					alleventdto.setPrice(Double.toString(listOfUpcomingEvents.get(i).getAllEvents().get(j).getExpectedPrice()));
					alleventdto.setNumberOfCustomers(Integer.toString(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventCustomerMappings().size()));
					alleventdto.setStartTime(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventStartTime().toString());
					alleventdto.setEndTime(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventEndTime().toString());
					alleventdto.setCommittedPower(Double.toString(listOfUpcomingEvents.get(i).getAllEvents().get(j).getCommitedPower()));
					alleventdto.setShortfall("0");
					alleventdto.setActualPower(Double.toString(listOfUpcomingEvents.get(i).getAllEvents().get(j).getActualPower()));
					alleventdto.setStartTime(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventStartTime().toString());
					List<EventCustomerMapping> listOfCustomers = eventcustomerrepo.getEventCustomerMappings(listOfUpcomingEvents.get(i).getAllEvents().get(j).getEventId());
					List<EventCustomerDto> listOfCustomersdto = new ArrayList<EventCustomerDto>();
					for(int k=0;k<listOfCustomers.size();k++) {
						EventCustomerDto eventcustomerdto = new EventCustomerDto();
						eventcustomerdto.setUserId(listOfCustomers.get(k).getAllUser().getUserId());
						eventcustomerdto.setUserName(listOfCustomers.get(k).getAllUser().getFullName());
						if (new Double(listOfCustomers.get(k).getActualPower()) !=null) {
							eventcustomerdto.setActualPower(listOfCustomers.get(k).getActualPower());	
						}
						if (new Double(listOfCustomers.get(k).getCommitedPower() )!=null) {
						eventcustomerdto.setCommitments(listOfCustomers.get(k).getCommitedPower());
						}
						eventcustomerdto.setPrice(listOfCustomers.get(k).getBidPrice());
						eventcustomerdto.setIsSelected("Y");
						// eventCustomerDto.setActualPower(allevent.getActualPower());
						eventcustomerdto.setParticipationStatus("1");
						listOfCustomersdto.add(eventcustomerdto);
						
					}
					alleventdto.setListOfCustomers(listOfCustomersdto);
					listOfEvents.add(alleventdto);
				}
			   alleventsetdto.setAllEvents(listOfEvents);
			   listOfUpcomingEventSetDto.add(alleventsetdto);
		   }
		   internalresponse.put("weeklyEvents", listOfWeeklyEventSetDto);
		   internalresponse.put("monthlyEvents", listOfMonthlyEventSetDto);
		   internalresponse.put("upcomingEvents", listOfUpcomingEventSetDto);
		   internalresponse.put("userId", alluser.getUserId());
		   response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("response", internalresponse);
			response.put("customMessage", CustomMessages.getCustomMessages("SL"));
	   
	   }
	   else if(count < 1) {
		   response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("customMessage", CustomMessages.getCustomMessages("WL"));
			response.put("response", internalresponse);
	   }
	    
	    }
	    catch (Exception e) {
	        System.out.println("Error in checkExistence" + e.getMessage());
	        e.printStackTrace();
	        response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error");
			response.put("response", null);
			response.put("customMessage", null);
	       
	    }
	    return response;
	}
	
	public HashMap<String,Object> updateEvent(List<Integer> events, int eventSetId) {
        
    	HashMap<String,Object> response=new HashMap<String, Object>();
        try {
        	
        		for(int i=0;i<events.size();i++) {
        	    	   eventrepo.updateEvent(2, events.get(i));
        	    	   eventcustomerrepo.updateEventCustomer(2, events.get(i));
        	          }	
          int eventCount = eventrepo.getEventByStatusId(events.get(0));
          if(eventCount >0) {
        	  eventsetrepo.updateEvent(3, eventSetId);
          } else {
        	  eventsetrepo.updateEvent(2, eventSetId);
          }
			response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("response",null);
			response.put("customMessage", null);
			

       }
        
        
        catch (Exception e) {
            System.out.println("Error in checkExistence" + e.getMessage());
            e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("response",null);
			response.put("customMessage", null);
			
        }
        return response;
    }
	
	
public HashMap<String,Object> cancelEvent(int event) {
        
    	HashMap<String,Object> response=new HashMap<String, Object>();
        try {
        	
        	    	   eventrepo.updateEvent(4, event);
        	    	   //eventcustomerrepo.updateEventCustomer(2, events.get(i));
        	
			response.put("responseStatus", "1");
			response.put("responseMessage", "The request was successfully served.");
			response.put("response",null);
			response.put("customMessage", null);
			

       }
        
        
        catch (Exception e) {
            System.out.println("Error in checkExistence" + e.getMessage());
            e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("response",null);
			response.put("customMessage", null);
			
        }
        return response;
    }

	public HashMap<String,Object> updateCustomer(List<Integer> customer,List<Integer> events) {
        
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	List<EventCustomerMapping> listOfEventCustMapping = new ArrayList<EventCustomerMapping>();
        try {
        	for (int i=0;i<events.size();i++) {
        	eventcustomerrepo.removeCustomers(events.get(i));
        	int count = eventcustomerrepo.getEventCustomerCount();
        	AllEvent allevent = eventrepo.getEventById(events.get(i));
        	for (int j=0;j<customer.size();j++) {
				count++;
				AllUser alluser =alluserrepo.getUserById(customer.get(j));
				//EventCustomerDto eventCustomerDto = new EventCustomerDto();
				EventCustomerMapping eventCustomerMapping = new EventCustomerMapping();
				eventCustomerMapping.setAllUser(alluser);
				eventCustomerMapping.setEventCustomerMappingId(count);
				eventCustomerMapping.setAllEvent(allevent);
				listOfEventCustMapping.add(eventCustomerMapping);
		}
        	}
		eventcustomerrepo.saveAll(listOfEventCustMapping);
		response.put("responseStatus", "1");
		response.put("responseMessage", "The request was successfully served.");
		response.put("response", null);
		response.put("customMessage", null);

       }
        
        
        catch (Exception e) {
            System.out.println("Error in checkExistence" + e.getMessage());
            e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("response", null);
			response.put("customMessage", null);

           
        }
        return response;
    }

	public HashMap<String,Object> getCustomerForEvents(List<Integer> events) {
        
		List<EventCustomerDto> listOfEventCustDto = new ArrayList<EventCustomerDto>();
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	HashMap<String,Object> internalResponse=new HashMap<String, Object>();
        try {
        
        	if(events.size() > 1) {
        		List<UserAccessLevelMapping> listOfCustomers = eventcustomerrepo.getUserAccessLevel();
        		for (int i=0;i<events.size();i++) {
        		for (int j=0;j<listOfCustomers.size();j++) {
        			AllEvent allevent = eventrepo.getEventById(events.get(i));
					EventCustomerDto eventCustomerDto = new EventCustomerDto();
					eventCustomerDto.setUserId(listOfCustomers.get(j).getAllUser().getUserId());
					eventCustomerDto.setUserName(listOfCustomers.get(j).getAllUser().getFullName());
					eventCustomerDto.setIsSelected("N");
					eventCustomerDto.setActualPower(allevent.getActualPower());
					eventCustomerDto.setParticipationStatus("1"); //hardcoded
					eventCustomerDto.setCommitments(allevent.getCommitedPower());
					eventCustomerDto.setPrice(allevent.getExpectedPrice());
				//	if (listOfCustomers.get(j).)
					// eventCustomerDto.setEventId(event.getEventId());
					listOfEventCustDto.add(eventCustomerDto);
			}
        		}
        	} else {
        		AllEvent allevent = eventrepo.getEventById(events.get(0));
        		List<UserAccessLevelMapping> listOfCustomers = eventcustomerrepo.getUserAccessLevel(allevent.getAllEventSet().getAllUser().getUserId());
        		List<EventCustomerMapping> listOfEventCustomers = eventcustomerrepo.getEventCustomerMappings(events.get(0));
        		for (int j=0;j<listOfCustomers.size();j++) {
        			EventCustomerDto eventCustomerDto = new EventCustomerDto();
        			if (listOfEventCustomers.size() > 0) {
        				
        			if (CompareHelper.compareData(listOfEventCustomers, listOfCustomers.get(j).getAllUser().getUserId()) != null) 
        			{
        			EventCustomerMapping evtmap =CompareHelper.compareData(listOfEventCustomers, listOfCustomers.get(j).getAllUser().getUserId());
        				eventCustomerDto.setIsSelected("Y");
//        			} else {
//        				eventCustomerDto.setIsSelected("N");
//        			}
        			
					
					eventCustomerDto.setUserId(listOfCustomers.get(j).getAllUser().getUserId());
					eventCustomerDto.setUserName(listOfCustomers.get(j).getAllUser().getFullName());
					eventCustomerDto.setActualPower(allevent.getActualPower());
					if(evtmap.getEventCustomerStatusId() >= 3) {
						eventCustomerDto.setParticipationStatus("1"); //hardcoded
					} else {
						eventCustomerDto.setParticipationStatus("0"); //hardcoded
					}
					if (evtmap.getCounterBidFlag() != null) {
						eventCustomerDto.setCounterBidFlag(evtmap.getCounterBidFlag()); //hardcoded
					} 
					eventCustomerDto.setCouterBidAmount(evtmap.getCounterBidAmount()); //hardcoded
					eventCustomerDto.setCommitments(evtmap.getCommitedPower());
					eventCustomerDto.setPrice(evtmap.getBidPrice());
					eventCustomerDto.setStatus(evtmap.getEventCustomerStatusId());
					// eventCustomerDto.setEventId(event.getEventId());
					listOfEventCustDto.add(eventCustomerDto);
        			}
			}	
        	}
        	}
        
		
        internalResponse.put("customers",listOfEventCustDto);
		response.put("responseStatus", "1");
		response.put("responseMessage", "The request was successfully served.");
		response.put("response", internalResponse);
		response.put("customMessage", null);

        	}
        	
        
        
        catch (Exception e) {
            System.out.println("Error in checkExistence" + e.getMessage());
            e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("response", null);
			response.put("customMessage", null);

           
        }
        return response;
    }
	
	public ArrayList<String> getPower(List<AllEventDto> listOfEvents) {
		ArrayList<String> response= new ArrayList<String>();
		double power=0; double price =0;
		try {
		for(int i=0;i<listOfEvents.size();i++) {
			power = power + Double.parseDouble(listOfEvents.get(i).getPlannedPower());
			price = price + Double.parseDouble(listOfEvents.get(i).getPrice());
		}
		response.add(Double.toString(power));
		response.add(Double.toString(price));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
public HashMap<String,Object> rejectCustomer(int eventId, int customerId) {
        
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	
        try {
        EventCustomerMapping eventcustomermapping = eventcustomerrepo.getEventCustomerById(eventId,customerId);
        eventcustomerrepo.updateEventCustomerbyId(6, eventId, customerId);
        eventrepo.removeEventPower(eventcustomermapping.getCommitedPower(), eventId);
        	response.put("responseStatus", "1");
		response.put("responseMessage", "The request was successfully served.");
		response.put("response", null);
		response.put("customMessage", null);

       }
        
        
        catch (Exception e) {
            System.out.println("Error in checkExistence" + e.getMessage());
            e.printStackTrace();
			response.put("responseStatus", "2");
			response.put("responseMessage", "Internal Server Error.");
			response.put("response", null);
			response.put("customMessage", null);

           
        }
        return response;
    }

public HashMap<String,Object> acceptCounterBid(int eventId, int customerId) {
    
	HashMap<String,Object> response=new HashMap<String, Object>();
	
    try {
    EventCustomerMapping eventcustomermapping = eventcustomerrepo.getEventCustomerById(eventId,customerId);
    eventcustomerrepo.acceptCounterBid("Y",5, eventId, customerId);
    eventrepo.addEventPower(eventcustomermapping.getCommitedPower(), eventId);
    	response.put("responseStatus", "1");
	response.put("responseMessage", "The request was successfully served.");
	response.put("response", null);
	response.put("customMessage", null);

   }
    
    
    catch (Exception e) {
        System.out.println("Error in checkExistence" + e.getMessage());
        e.printStackTrace();
		response.put("responseStatus", "2");
		response.put("responseMessage", "Internal Server Error.");
		response.put("response", null);
		response.put("customMessage", null);

       
    }
    return response;
}
}
