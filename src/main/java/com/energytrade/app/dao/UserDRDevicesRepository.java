package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.UserDRDevices;
import com.energytrade.app.model.UserDevice;

@Repository
public interface UserDRDevicesRepository extends JpaRepository<UserDRDevices, Long> {

	@Query("Select COALESCE(max(a.userDrDeviceId),0) from UserDRDevices a")
	int getDrDeviceCount();

}
