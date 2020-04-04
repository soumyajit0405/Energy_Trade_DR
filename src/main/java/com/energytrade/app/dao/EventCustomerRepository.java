package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.Modifying;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.AllElectricityBoard;
import com.energytrade.app.model.AllEventSet;
import com.energytrade.app.model.AllForecast;
import com.energytrade.app.model.AllOtp;
import com.energytrade.app.model.AllSellOrder;
import com.energytrade.app.model.AllState;
import com.energytrade.app.model.AllTimeslot;
import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.ContractStatusPl;
import com.energytrade.app.model.EventCustomerMapping;
import com.energytrade.app.model.EventSetStatusPl;
import com.energytrade.app.model.LocalityPl;
import com.energytrade.app.model.NonTradeHour;
import com.energytrade.app.model.NonTradehourStatusPl;
import com.energytrade.app.model.OrderStatusPl;
import com.energytrade.app.model.StateBoardMapping;
import com.energytrade.app.model.UserAccessLevelMapping;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.model.UserTypePl;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EventCustomerRepository extends JpaRepository<EventCustomerMapping, Long>
{
      
	  
	  @Query("Select max(eventCustomerMappingId) from EventCustomerMapping a ") 
	  int getEventCustomerCount();
	 
	  @Query("Select a from UserAccessLevelMapping a where a.userTypepl.userTypeId=2 and a.allUser.userId <> ?1") 
	  List<UserAccessLevelMapping> getUserAccessLevel(int userId);
	  
	  @Modifying
	  @Query("delete from EventCustomerMapping a where a.allEvent.eventId=?1") 
	  void removeCustomers(int eventId);
	 
	  @Query("Select a from UserAccessLevelMapping a where a.userTypepl.userTypeId=2") 
	  List<UserAccessLevelMapping> getUserAccessLevel();
	  
	  @Query("Select a from EventCustomerMapping a where a.allEvent.eventId=?1") 
	  List<EventCustomerMapping> getEventCustomerMappings(int eventId);
}