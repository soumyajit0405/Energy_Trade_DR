package com.energytrade.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


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
	private Double actualPower;

	@Column(name="counter_bid_flag")
	private String counterBidFlag;
	
	@Column(name="counter_bid_amount")
	private double counterBidAmount;
	
	public String getCounterBidFlag() {
		return counterBidFlag;
	}

	public void setCounterBidFlag(String counterBidFlag) {
		this.counterBidFlag = counterBidFlag;
	}

	public double getCounterBidAmount() {
		return counterBidAmount;
	}

	public void setCounterBidAmount(double counterBidAmount) {
		this.counterBidAmount = counterBidAmount;
	}

	@Column(name="bid_price")
	private double bidPrice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bid_ts")
	private Date bidTs;

	@Column(name="commited_power")
	private Double commitedPower;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_ts")
	private Date createdTs;

	@Column(name="counter_bid_flag")
	private String counterBidFlag;
	
	@Column(name="counter_bid_amount")
	private Double counterBidAmount;
	
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
	
	//bi-directional many-to-one association to EventCustomerMapping
	@OneToMany(mappedBy="eventCustomerMapping")
	private List<EventCustomerDevices> eventCustomerDevices;

	public EventCustomerMapping() {
	}

	public int getEventCustomerMappingId() {
		return eventCustomerMappingId;
	}

	public void setEventCustomerMappingId(int eventCustomerMappingId) {
		this.eventCustomerMappingId = eventCustomerMappingId;
	}

	public byte getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(byte activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Double getActualPower() {
		return actualPower;
	}

	public void setActualPower(Double actualPower) {
		this.actualPower = actualPower;
	}

	public double getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}

	public Date getBidTs() {
		return bidTs;
	}

	public void setBidTs(Date bidTs) {
		this.bidTs = bidTs;
	}

	public Double getCommitedPower() {
		return commitedPower;
	}

	public void setCommitedPower(Double commitedPower) {
		this.commitedPower = commitedPower;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public String getCounterBidFlag() {
		return counterBidFlag;
	}

	public void setCounterBidFlag(String counterBidFlag) {
		this.counterBidFlag = counterBidFlag;
	}

	public Double getCounterBidAmount() {
		return counterBidAmount;
	}

	public void setCounterBidAmount(Double counterBidAmount) {
		this.counterBidAmount = counterBidAmount;
	}

	public int getEventCustomerStatusId() {
		return eventCustomerStatusId;
	}

	public void setEventCustomerStatusId(int eventCustomerStatusId) {
		this.eventCustomerStatusId = eventCustomerStatusId;
	}

	public byte getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(byte softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public Date getSyncTs() {
		return syncTs;
	}

	public void setSyncTs(Date syncTs) {
		this.syncTs = syncTs;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public AllEvent getAllEvent() {
		return allEvent;
	}

	public void setAllEvent(AllEvent allEvent) {
		this.allEvent = allEvent;
	}

	public AllUser getAllUser() {
		return allUser;
	}

	public void setAllUser(AllUser allUser) {
		this.allUser = allUser;
	}

	public List<EventCustomerDevices> getEventCustomerDevices() {
		return eventCustomerDevices;
	}

	public void setEventCustomerDevices(List<EventCustomerDevices> eventCustomerDevices) {
		this.eventCustomerDevices = eventCustomerDevices;
	}

	
	
	

}