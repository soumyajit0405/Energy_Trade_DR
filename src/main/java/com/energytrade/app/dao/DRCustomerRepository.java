package com.energytrade.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.AllEvent;
import com.energytrade.app.model.AllEventSet;
import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.DRContracts;
import com.energytrade.app.model.EventCustomerMapping;
import com.energytrade.app.model.UserRolesPl;

@Repository
public interface DRCustomerRepository extends JpaRepository<AllUser, Long> {

	@Query("Select a from DRContracts a where a.contractNumber=?1")
	DRContracts getBusinessContractDetails(String contractNumber);

	@Query("Select count(a.userId) from AllUser a where a.drContractNumber=?1")
	int getUserDetailsfromContract(String drContractNumber);

	@Query("select distinct a from AllEventSet a, AllEvent b, EventCustomerMapping c where b.eventId = c.allEvent.eventId and a.eventSetId = b.allEventSet.eventSetId and c.allUser.userId = ?1")
	List<AllEventSet> getEventSetsForCustomer(int customerId);

	@Query("select a from AllEvent a, EventCustomerMapping b where a.eventId = b.allEvent.eventId and b.allUser.userId = ?1 and a.allEventSet.eventSetId = ?2")
	List<AllEvent> getEventsForCustomerAndEventSet(int customerId, int eventSetId);

	@Query("select a from EventCustomerMapping a where a.allEvent.eventId = ?2 and a.allUser.userId = ?1")
	EventCustomerMapping getEventCustomerMapping(int customerId, int eventId);

	@Modifying
	@Query("update AllUser a set a.fullName=?1,a.userRolesPl.userRoleId=2,a.drContractNumber=?2 where a.phoneNumber=?3")
	void updateDrUserDetails(String fullName, String drContractNumber, String phoneNumber);

	@Query("Select a from UserRolesPl a where a.userRoleName=?1")
	UserRolesPl getUserRole(String role);

	@Query("Select a from AllUser a where a.phoneNumber=?1")
	AllUser getUserIdByPhone(String phone);

}
