package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.Modifying;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.AllElectricityBoard;
import com.energytrade.app.model.AllEvent;
import com.energytrade.app.model.AllEventSet;
import com.energytrade.app.model.AllForecast;
import com.energytrade.app.model.AllOtp;
import com.energytrade.app.model.AllSellOrder;
import com.energytrade.app.model.AllState;
import com.energytrade.app.model.AllTimeslot;
import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.ContractStatusPl;
import com.energytrade.app.model.EventStatusPl;
import com.energytrade.app.model.LocalityPl;
import com.energytrade.app.model.NonTradeHour;
import com.energytrade.app.model.NonTradehourStatusPl;
import com.energytrade.app.model.OrderStatusPl;
import com.energytrade.app.model.StateBoardMapping;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.model.UserTypePl;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EventRepository extends JpaRepository<AllEvent, Long>
{
	@Query("Select max(eventId) from AllEvent a ") 
	  int getEventCount();
	
	@Query("Select a from EventStatusPl a where  a.name=?1") 
	  EventStatusPl getEventStatus(String eventStatusName);
	
	 @Modifying
	    @Query("update AllEvent a set a.eventStatusPl.eventStatusId=?1 where a.eventId=?2")
	     void updateEvent(int eventStatusId, int eventId);
	
	 @Query("Select COUNT(a.eventId) from AllEvent a where a.allEventSet.eventSetId=(select allEventSet.eventSetId from AllEvent where eventId=?1) and a.eventStatusPl.eventStatusId=1") 
	  int getEventByStatusId(int eventId);
	 
	 @Query("Select a from AllEvent a where  a.eventId=?1") 
	  AllEvent getEventById(int eventId);
	 
	 @Query("Select a from AllEventSet a where  a.uploadTime >=?1 and a.uploadTime <=?2 and a.eventSetId <>1") 
	  List<AllEventSet> getEventSetBydate(Date startDate, Date endDate);
	 
	 @Query("Select a from AllEventSet a where  a.uploadTime >=?1 and a.eventSetId <>1") 
	  List<AllEventSet> getupcomingEventSet(Date startDate);
	 
	 @Modifying
	    @Query("update AllEvent a set a.commitedPower=a.commitedPower+?1 where a.eventId=?2")
	     void addEventPower(double power, int eventId);
	 
	 @Modifying
	    @Query("update AllEvent a set a.commitedPower=a.commitedPower-?1 where a.eventId=?2")
	     void removeEventPower(double power, int eventId);
}