package com.energytrade.app.controller;

import java.util.Hashtable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.HashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.energytrade.app.services.DRService;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RestController
public class DRController extends AbstractBaseController
{
    @Autowired
    private DRService drservice;
    
	@RequestMapping(value=REST+"uploadEventSet/{location}/{userId}/{uploadDate}",method = RequestMethod.POST,headers="Accept=application/json")
    public HashMap<String,Object> uploadEventSet(@RequestBody HashMap<String,String> inputDetails, @PathVariable("location") String location, @PathVariable("userId") int userId,@PathVariable("uploadDate") String uploadDate)
    {
		HashMap<String,Object> response = new HashMap<String, Object>();
        try
        {
        	String imageDataArr=inputDetails.get("eventSet");
            //This will decode the String which is encoded by using Base64 class
            byte[] imageByte=Base64.decodeBase64(imageDataArr);
            String directory="/home/"+"sample.xlsx";
            //String directory="C:\\Soumyajit\\ET-files-20200417T033846Z-001\\ET-files\\EnergyTrade-DR\\"+"sample.xlsx";
            response =  drservice.createEventSet(directory, imageByte, location, userId, uploadDate);
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return response;
    }
	
	@RequestMapping(value=REST+"reUploadEventSet/{eventSetId}",method = RequestMethod.POST,headers="Accept=application/json")
    public HashMap<String,Object> reuploadEventSet(@RequestBody HashMap<String,String> inputDetails, @PathVariable("eventSetId") int eventSetId)
    {
		HashMap<String,Object> response = new HashMap<String, Object>();
        try
        {
        	String imageDataArr=inputDetails.get("eventSet");
            //This will decode the String which is encoded by using Base64 class
            byte[] imageByte=Base64.decodeBase64(imageDataArr);
            
            String directory="/home/"+"sample.xlsx";
            //String directory="C:\\Soumyajit\\ET-files-20200417T033846Z-001\\ET-files\\EnergyTrade-DR\\"+"sample.xlsx";
            response =  drservice.updateEventSet(directory, imageByte, eventSetId);
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return response;

    }
	
	@RequestMapping(value=REST+"loginUser",method = RequestMethod.POST,headers="Accept=application/json")
    public HashMap<String,Object>  loginUser(@RequestBody HashMap<String,String> inputDetails)
    {
		HashMap<String,Object> response= new HashMap<String, Object>();
        try
        {
        	
           response = drservice.loginUser(inputDetails.get("phoneNumber"), inputDetails.get("password"));
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            //return "error = "+e;
        }
        return response;

    }
	

    @RequestMapping(value =REST+"loginDSOUser" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> loginDSOUser(@RequestBody HashMap<String,String> inputDetails) throws ParseException {
        
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.loginDSOUser(inputDetails.get("email"), inputDetails.get("password")));
    	return response;
    }
    
    @RequestMapping(value =REST+"publishEvent" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> updateEvent(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.updateEvent((List<Integer>)events.get("eventIdList"),(int)events.get("eventSetId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"updateCustomersForEvent" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> updateCustomers(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.updateCustomer((List<Integer>)events.get("customers"),(List<Integer>)events.get("eventId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"getEventOverview" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> getEventDetails(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.getEventDetails((List<Integer>)events.get("eventId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"getEvents" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> getEventSetDetails(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.getEventSetDetails((int)events.get("eventSetId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"getCustomerForEvents" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> getCustomerForEvents(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.getCustomerForEvents((List<Integer>)events.get("events")));
    	return response;
    }
    
    @RequestMapping(value =REST+"rejectCustomer" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> rejectCustomer(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.rejectCustomer((int)events.get("eventId"),(int)events.get("eventCustomerId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"rejectCounterBid" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> rejectCounterBid(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.rejectCounterBid((int)events.get("eventId"),(int)events.get("eventCustomerId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"acceptCounterBid" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> acceptCounterBid(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.acceptCounterBid((int)events.get("eventId"),(int)events.get("eventCustomerId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"cancelEvent" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> cancelEvent(@RequestBody HashMap<String,Object> events) {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.cancelEvent((int)events.get("eventId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"getEventSets" , method =  RequestMethod.POST , headers =  "Accept=application/json" )
    public HashMap<String,Object> getEventSetsByUser(@RequestBody HashMap<String,Object> events) throws ParseException {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.getEventSetsByUser((int)events.get("userId")));
    	return response;
    }
    
    @RequestMapping(value =REST+"getDsoDetails/{dsoId}" , method =  RequestMethod.GET , headers =  "Accept=application/json" )
    public HashMap<String,Object> getDsoDetails(@PathVariable("dsoId") int dsoId) throws ParseException {
    	HashMap<String,Object> response=new HashMap<String, Object>();
    	response.put("response", drservice.getDsoDetails(dsoId));
    	return response;
    }
    
}
