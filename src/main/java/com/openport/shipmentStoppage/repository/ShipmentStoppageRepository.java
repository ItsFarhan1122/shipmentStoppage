package com.openport.shipmentStoppage.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openport.shipmentStoppage.entity.ShipmentStoppage;

@Repository
public interface ShipmentStoppageRepository extends JpaRepository<ShipmentStoppage,Integer> {
	
//	In-line query
	@Query(value ="select od.order_detail_id,od.create_dt,od.order_id,o.shipper_id,od.status,"
			+ " conf.col_value,od.dispatch_number from opentm_db.order_details  od left join opentm_db.orders o "
			+ " ON (od.order_id= o.order_id) left join opentm_db.order_events oe "
			+ " ON(od.order_id=oe.order_id)left join openport.shippers sh ON(o.shipper_id=sh.id)"
			+ " left join openport.configuration conf ON(sh.reference_opentm_id=conf.col_name ) "
			+ " where oe.event_id=7 and sh.is_calculate_stoppage='Y' limit 2;", nativeQuery = true)
	public List<String> GetOrdersData();

	
	
	@Query(value ="SELECT count(order_event_id) FROM opentm_db.order_events WHERE  order_id in (:orderId) and event_id=11 limit 1;", nativeQuery = true)
	public int checkEvent(@Param(value = "orderId") String orderId);


	@Query(value ="select event_date,event_id from opentm_db.order_events where order_id=:orderId and event_id in(7,11);", nativeQuery = true)
	public List<String> getEventDate(@Param(value = "orderId") String orderId);
	
	
	@Query(value ="	select od.order_id from  opentm_db.order_details od  left join"
			+ "  opentm_db.shipment_stoppage sst ON (od.order_id=sst.order_id) where sst.is_stopped='Y' and od.status <>7;", nativeQuery = true)
	public String getOrderIdWithoutIntransitStatus();
	
	
	@Query(value ="update opentm_db.shipment_stoppage set is_stopped='N',update_dt=:updateDt where order_id=:orderId;", nativeQuery = true)
	public void updateOrderWithIntransitStatus(@Param(value = "orderId") String orderId,@Param(value="updateDt") Timestamp updateDt);
	
	
	@Query(value ="	select count(*) from opentm_db.shipment_stoppage where order_id:orderId", nativeQuery = true)
	public int checkShipmentStoppageExist(@Param(value = "orderId") String orderId);

	@Query(value ="	update opentm_db.shipment_stoppage set total_stoppage_time=:totalStoppageTime,stoppage_count=:stoppageCount, is_stopped=:isStopped,update_dt=:updateDt where order_id=:orderId", nativeQuery = true)
	public void updateShipmentStoppage(@Param(value = "orderId") String orderId,
			@Param(value="updateDt") Timestamp updateDt,
			@Param(value="stoppageCount") int stoppageCount,
			@Param(value="totalStoppageTime") Double totalStoppageTime,
			@Param(value="isStopped") String isStopped);


}
