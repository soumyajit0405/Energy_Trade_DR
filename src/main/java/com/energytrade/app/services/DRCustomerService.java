package com.energytrade.app.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.energytrade.app.dao.DRCustomerDao;
import com.energytrade.app.dao.EventCustomerMappingDao;

@Service("eventCustomerMappingService")
public class DRCustomerService extends AbstractBaseService {

	@Autowired
	private EventCustomerMappingDao eventCustomerMappingDao;

	@Autowired
	private DRCustomerDao drCustomerDao;

	public HashMap<String, Object> participateInEvent(HashMap<String, Object> inputDetails) {
		return this.eventCustomerMappingDao.participateInEvent(inputDetails);
	}

	public HashMap<String, Object> withdrawFromEvent(int userId, int eventId) {
		return this.eventCustomerMappingDao.withdrawFromEvent(userId, eventId);
	}

	public HashMap<String, Object> updateEventCommitments(int userId, int eventId, double updatedCommitedPower,
			double updatedBidPrice) {
		return this.eventCustomerMappingDao.updateEventCommitments(userId, eventId, updatedCommitedPower,
				updatedBidPrice);
	}

	public HashMap<String, Object> getBusinessContractDetails(String contractId) {
		return this.drCustomerDao.getBusinessContractDetails(contractId);
	}

	public HashMap<String, Object> updateDrUserDetails(String phone, String fullName, String drContractNumber) {
		return this.drCustomerDao.updateDrUserDetails(phone, fullName, drContractNumber);
	}

	public HashMap<String, Object> getEventSetsForCustomer(int customerId) {
		return this.drCustomerDao.getEventSetsForCustomer(customerId);
	}

}
