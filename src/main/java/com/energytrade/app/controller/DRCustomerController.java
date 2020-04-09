package com.energytrade.app.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.energytrade.app.services.DRCustomerService;

@CrossOrigin
@RestController
public class DRCustomerController extends AbstractBaseController {

	@Autowired
	DRCustomerService drCustomerService;

	@RequestMapping(value = "health", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Object> healthCheck() {

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("msg", "Server is up");
		return response;
	}

	@RequestMapping(value = "updateDrCustomerDetails", method = RequestMethod.POST, headers = "Accept=application/json")
	public HashMap<String, Object> updateDrCustomerDetails(@RequestBody HashMap<String, Object> inputDetails) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("response", drCustomerService.updateDrUserDetails((String) inputDetails.get("phoneNumber"),
				(String) inputDetails.get("fullName"), (String) inputDetails.get("drContractNumber")));
		return response;
	}

	@RequestMapping(value = "getBusinessContractDetails/{contractId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Object> getBusinessContractDetails(@PathVariable("contractId") String contractId) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("response", drCustomerService.getBusinessContractDetails(contractId));
		return response;
	}

	@RequestMapping(value = "getEventSetsForCustomer/{customerId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Object> getEventSetsForCustomer(@PathVariable("customerId") int customerId) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("response", drCustomerService.getEventSetsForCustomer(customerId));
		return response;
	}

	@RequestMapping(value = "getEventsForCustomerAndEventSet", method = RequestMethod.POST, headers = "Accept=application/json")
	public HashMap<String, Object> getEventsForCustomerAndEventSet(@RequestBody HashMap<String, Object> inputDetails) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("response", drCustomerService.getEventsForCustomerAndEventSet((int) inputDetails.get("customerId"),
				(int) inputDetails.get("eventSetId")));
		return response;
	}

}
