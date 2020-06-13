package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.EventSetVersionHistory;

@Repository
public interface EventSetVersionHistoryRepository extends JpaRepository<EventSetVersionHistory, Long> {
	@Query("Select COALESCE(max(version),0) from EventSetVersionHistory a where a.eventSetId=?1") 
	  int getLatestEventSetVersion(int eventSetId);
}
