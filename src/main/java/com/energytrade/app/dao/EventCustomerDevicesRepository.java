
package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.EventCustomerDevices;


@Repository
public interface EventCustomerDevicesRepository extends JpaRepository<EventCustomerDevices, Long> {

}
