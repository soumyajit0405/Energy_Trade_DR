
package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.EventCustomerDevices;
import com.energytrade.app.model.EventCustomerMapping;


@Repository
public interface EventCustomerDevicesRepository extends JpaRepository<EventCustomerDevices, Long> {
	
	@Query("Select a from EventCustomerDevices a where a.eventCustomerMapping.eventCustomerMappingId=?1") 
	EventCustomerDevices getEventCustomerDeviceById(int eventCustomerMappingId);
}
