package com.openport.shipmentStoppage.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * @author Farhan Rana
 *
 */

@Entity
@Table(name = "shipment_stoppage")
public class ShipmentStoppage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stoppage_id")
    private Long stoppageId;

    @Column(name = "total_stoppage_time")
    private Double totalStoppageTime;

    @Column(name = "stoppage_count")
    private Integer stoppageCount;

    @Column(name = "is_stopped")
    private String isStopped;

    @Column(name = "order_id")
    private Long orderID;

    @Column(name = "create_dt")
    private Timestamp createDt;

    @Column(name = "update_dt")
    private Timestamp updateDt;

    public ShipmentStoppage() {
    }

    public ShipmentStoppage(Double totalStoppageTime, Integer stoppageCount, String isStopped, Long orderID, Timestamp createDt, Timestamp updateDt) {
        this.totalStoppageTime = totalStoppageTime;
        this.stoppageCount = stoppageCount;
        this.isStopped = isStopped;
        this.orderID = orderID;
        this.createDt = createDt;
        this.updateDt = updateDt;
    }

	public Long getStoppageId() {
		return stoppageId;
	}

	public void setStoppageId(Long stoppageId) {
		this.stoppageId = stoppageId;
	}

	public Double getTotalStoppageTime() {
		return totalStoppageTime;
	}

	public void setTotalStoppageTime(Double totalStoppageTime) {
		this.totalStoppageTime = totalStoppageTime;
	}

	public Integer getStoppageCount() {
		return stoppageCount;
	}

	public void setStoppageCount(Integer stoppageCount) {
		this.stoppageCount = stoppageCount;
	}

	public String getIsStopped() {
		return isStopped;
	}

	public void setIsStopped(String isStopped) {
		this.isStopped = isStopped;
	}

	public Long getorderID() {
		return orderID;
	}

	public void setorderID(Long shipmentId) {
		this.orderID = orderID;
	}

	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public Timestamp getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Timestamp updateDt) {
		this.updateDt = updateDt;
	}

	@Override
	public String toString() {
		return "ShipmentStoppage [stoppageId=" + stoppageId + ", totalStoppageTime=" + totalStoppageTime
				+ ", stoppageCount=" + stoppageCount + ", isStopped=" + isStopped + ", shipmentId=" + orderID
				+ ", createDt=" + createDt + ", updateDt=" + updateDt + "]";
	}
    
    

}


