package com.energytrade.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the event_customer_mapping database table.
 * 
 */
@Entity
@Table(name="event_customer_mapping")
@NamedQuery(name="EventCustomerMapping.findAll", query="SELECT e FROM EventCustomerMapping e")
public class EventCustomerMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="event_customer_mapping_id")
	private int eventCustomerMappingId;

	@Column(name="active_status")
	private byte activeStatus;

	@Column(name="actual_power")
	private double actualPower;

	@Column(name="bid_price")
	private double bidPrice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bid_ts")
	private Date bidTs;

	@Column(name="commited_power")
	private double commitedPower;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_ts")
	private Date createdTs;

	@Column(name="event_customer_status_id")
	private int eventCustomerStatusId;

	private byte softdeleteflag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sync_ts")
	private Date syncTs;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_ts")
	private Date updatedTs;

	//bi-directional many-to-one association to AllEvent
	@ManyToOne
	@JoinColumn(name="event_id")
	private AllEvent allEvent;

	//bi-directional many-to-one association to AllUser
	@ManyToOne
	@JoinColumn(name="customer_id")
	private AllUser allUser;

	public EventCustomerMapping() {
	}

	public int getEventCustomerMappingId() {
		return this.eventCustomerMappingId;
	}

	public void setEventCustomerMappingId(int eventCustomerMappingId) {
		this.eventCustomerMappingId = eventCustomerMappingId;
	}

	public byte getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(byte activeStatus) {
		this.activeStatus = activeStatus;
	}

	public double getActualPower() {
		return this.actualPower;
	}

	public void setActualPower(double actualPower) {
		this.actualPower = actualPower;
	}

	public double getBidPrice() {
		return this.bidPrice;
	}

	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public Date getBidTs() {
		return this.bidTs;
	}

	public void setBidTs(Date bidTs) {
		this.bidTs = bidTs;
	}

	public double getCommitedPower() {
		return this.commitedPower;
	}

	public void setCommitedPower(double commitedPower) {
		this.commitedPower = commitedPower;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTs() {
		return this.createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public int getEventCustomerStatusId() {
		return this.eventCustomerStatusId;
	}

	public void setEventCustomerStatusId(int eventCustomerStatusId) {
		this.eventCustomerStatusId = eventCustomerStatusId;
	}

	public byte getSoftdeleteflag() {
		return this.softdeleteflag;
	}

	public void setSoftdeleteflag(byte softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public Date getSyncTs() {
		return this.syncTs;
	}

	public void setSyncTs(Date syncTs) {
		this.syncTs = syncTs;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTs() {
		return this.updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public AllEvent getAllEvent() {
		return this.allEvent;
	}

	public void setAllEvent(AllEvent allEvent) {
		this.allEvent = allEvent;
	}

	public AllUser getAllUser() {
		return this.allUser;
	}

	public void setAllUser(AllUser allUser) {
		this.allUser = allUser;
	}

}