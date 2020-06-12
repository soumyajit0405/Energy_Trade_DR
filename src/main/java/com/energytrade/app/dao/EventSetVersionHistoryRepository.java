package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.EventSetVersionHistory;

@Repository
public interface EventSetVersionHistoryRepository extends JpaRepository<EventSetVersionHistory, Long> {

}
