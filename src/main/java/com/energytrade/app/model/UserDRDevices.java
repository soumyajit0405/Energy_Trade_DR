package com.energytrade.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user_dr_devices")
@NamedQuery(name = "UserDRDevices.findAll", query = "SELECT a FROM UserDRDevices a")
public class UserDRDevices {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_dr_device_id")
	private int userDrDeviceId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AllUser allUser;

	@Column(name = "device_name")
	private String deviceName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_ts")
	private Date createdTs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_ts")
	private Date updatedTs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sync_ts")
	private Date syncTs;

	private byte softdeleteflag;

	@Column(name = "device_capacity")
	private Double device_capacity;

}
