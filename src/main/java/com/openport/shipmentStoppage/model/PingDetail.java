package com.openport.shipmentStoppage.model;

public class PingDetail {
	
     private String truckNumber;
	 private String latitude;
	 private String longitude;
	 private String pingDate;
	 
	 
	public String getTruckNumber() {
		return truckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPingDate() {
		return pingDate;
	}
	public void setPingDate(String pingDate) {
		this.pingDate = pingDate;
	}
	@Override
	public String toString() {
		return "PingDetail [truckNumber=" + truckNumber + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", pingDate=" + pingDate + "]";
	}
	
	 
	 

}
