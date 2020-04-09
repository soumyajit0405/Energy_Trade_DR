package com.energytrade.app.dto;

public class DRDeviceDto extends AbstractBaseDto {

	private int drDeviceId;
	private String drDeviceName;
	private double deviceCapacity;

	public int getDrDeviceId() {
		return drDeviceId;
	}

	public void setDrDeviceId(int drDeviceId) {
		this.drDeviceId = drDeviceId;
	}

	public String getDrDeviceName() {
		return drDeviceName;
	}

	public void setDrDeviceName(String drDeviceName) {
		this.drDeviceName = drDeviceName;
	}

	public double getDeviceCapacity() {
		return deviceCapacity;
	}

	public void setDeviceCapacity(double deviceCapacity) {
		this.deviceCapacity = deviceCapacity;
	}

}
