package com.openport.shipmentStoppage.model;

import java.sql.Timestamp;

public class OrderDetail {
	
	private String orderDetailId;
	private Timestamp createDt;
	private String orderId;
	private String shipperId; 
	private String dispatchNumber;
	private String status;
	private String stoppageHour;
	private String eventDt;
	private String eventId;
	
	
	
	
	public String getDispatchNumber() {
		return dispatchNumber;
	}
	public void setDispatchNumber(String dispatchNumber) {
		this.dispatchNumber = dispatchNumber;
	}
	public String getEventDt() {
		return eventDt;
	}
	public void setEventDt(String eventDt) {
		this.eventDt = eventDt;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Timestamp getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getShipperId() {
		return shipperId;
	}
	public void setShipperId(String shipperId) {
		this.shipperId = shipperId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStoppageHour() {
		return stoppageHour;
	}
	public void setStoppageHour(String stoppageHour) {
		this.stoppageHour = stoppageHour;
	}
	@Override
	public String toString() {
		return "OrderDetail [orderDetailId=" + orderDetailId + ", createDt=" + createDt + ", orderId=" + orderId
				+ ", shipperId=" + shipperId + ", dispatchNumber=" + dispatchNumber + ", status=" + status
				+ ", stoppageHour=" + stoppageHour + ", eventDt=" + eventDt + ", eventId=" + eventId + "]";
	}

	
	
	
}
