package com.energytrade.app.dao;

import org.springframework.data.jpa.repository.Modifying;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.energytrade.app.model.AllDso;
import com.energytrade.app.model.AllElectricityBoard;
import com.energytrade.app.model.AllKiotSwitch;
import com.energytrade.app.model.AllState;
import com.energytrade.app.model.AllUser;
import com.energytrade.app.model.KiotUserMapping;
import com.energytrade.app.model.LocalityPl;
import com.energytrade.app.model.StateBoardMapping;
import com.energytrade.app.model.UserRolesPl;
import com.energytrade.app.model.UserTypePl;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AllKiotSwitchesRepository extends JpaRepository<AllKiotSwitch, Long>
{
    
    @Query("select a from AllKiotSwitch a where a.kiotUserMapping.kiotUserMappingId=?1 and a.usedFlag='N'")
    List<AllKiotSwitch> getAllKiotSwitches(int kiotUserMappingId);
    
    @Modifying
    @Query("update AllKiotSwitch a set a.usedFlag='Y' where a.id=?1")
    void updateKiotSwicthes(int id);
    
    @Query("select a from AllKiotSwitch a where a.id=?1")
    AllKiotSwitch getAllKiotSwitchesById(int id);
          
}