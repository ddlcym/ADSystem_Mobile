package com.changhong.adsystem.model;

import java.io.Serializable;

public class DeviceInfor implements Serializable{
	public String id;//设备ID
	public String mac;//设备mac地址
	public String ssid;//热点SSID
	public String psw;//热点密码

	private String adResouseId ;//
	private int appVersion;//
	private long memoryAvailable;
	private long memoryTotal;
	private String stbSoftwareVersion;
	private String stbHardwareVersion;
	public String getAdResouseId() {
		return adResouseId;
	}
	public void setAdResouseId(String adResouseId) {
		this.adResouseId = adResouseId;
	}
	public int getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public long getMemoryAvailable() {
		return memoryAvailable;
	}
	public void setMemoryAvailable(long memoryAvailable) {
		this.memoryAvailable = memoryAvailable;
	}
	public long getMemoryTotal() {
		return memoryTotal;
	}
	public void setMemoryTotal(long memoryTotal) {
		this.memoryTotal = memoryTotal;
	}
	public String getStbSoftwareVersion() {
		return stbSoftwareVersion;
	}
	public void setStbSoftwareVersion(String stbSoftwareVersion) {
		this.stbSoftwareVersion = stbSoftwareVersion;
	}
	public String getStbHardwareVersion() {
		return stbHardwareVersion;
	}
	public void setStbHardwareVersion(String stbHardwareVersion) {
		this.stbHardwareVersion = stbHardwareVersion;
	}
	
	
	
	
}
