package com.energytrade.app.dao;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.util.CustomMessages;

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
}
