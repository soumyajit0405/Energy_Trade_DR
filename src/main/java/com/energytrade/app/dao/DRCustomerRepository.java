package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.DRContracts;
import com.energytrade.app.model.UserRolesPl;

@Repository
public interface DRCustomerRepository extends JpaRepository<AllUser, Long> {

	@Query("Select a from DRContracts a where a.contractNumber=?1")
	DRContracts getBusinessContractDetails(String contractNumber);
	
	@Query("Select count(a.userId) from AllUser a where a.drContractNumber=?1")
	int getUserDetailsfromContract(String drContractNumber);

	@Modifying
	@Query("update AllUser a set a.fullName=?1,a.userRolesPl.userRoleId=2,a.drContractNumber=?2 where a.phoneNumber=?3")
	void updateDrUserDetails(String fullName, String drContractNumber, String phoneNumber);

	@Query("Select a from UserRolesPl a where a.userRoleName=?1")
	UserRolesPl getUserRole(String role);

	@Query("Select a from AllUser a where a.phoneNumber=?1")
	AllUser getUserIdByPhone(String phone);

}
