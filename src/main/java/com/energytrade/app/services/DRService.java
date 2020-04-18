package com.energytrade.app.services;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.energytrade.app.dao.DRDao;


@Service("DRService")
public class DRService extends AbstractBaseService
{
    @Autowired
    private DRDao drdao;
    
    public HashMap<String,Object> createEventSet(String filePath, byte [] imageByte, String location, int userId) {
         return this.drdao.createEventSet(filePath, imageByte, location, userId);
    }
          
    public HashMap<String, Object> loginUser(String phoneNumber, String password) {
    	return drdao.loginUser(phoneNumber, password);
    }
    
    public HashMap<String,Object> loginDSOUser(String email, String password) throws ParseException {
        return this.drdao.loginDSOUser(email,password);
    }
    
    public HashMap<String,Object> updateEvent(List<Integer> events, int eventSetId) {
    	return drdao.updateEvent(events, eventSetId);
    }
    
    public HashMap<String,Object> updateCustomer(List<Integer> customer,List<Integer> eventId) {
    	return drdao.updateCustomer(customer,eventId);
    }
    
    public HashMap<String, Object> getEventDetails(List<Integer> eventId){
    	return drdao.getEventDetails(eventId);
    }
    
    public HashMap<String, Object> getEventSetDetails(int eventSetId) {
    	return drdao.getEventSetDetails(eventSetId);
    }
    
    public HashMap<String,Object> getCustomerForEvents(List<Integer> events){
    	return drdao.getCustomerForEvents(events);
    }
    
    public HashMap<String,Object> rejectCustomer(int eventId, int customerId) {
    	return drdao.rejectCustomer(eventId, customerId);
    }
    
    public HashMap<String,Object> acceptCounterBid(int eventId, int customerId) {
    	return drdao.acceptCounterBid(eventId, customerId);
    }
    
    public HashMap<String,Object> cancelEvent(int event) {
    	return drdao.cancelEvent(event);
    }
}