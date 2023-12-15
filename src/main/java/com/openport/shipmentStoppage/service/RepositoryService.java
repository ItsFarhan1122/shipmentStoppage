package com.openport.shipmentStoppage.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.openport.shipmentStoppage.entity.ShipmentStoppage;
import com.openport.shipmentStoppage.model.OrderDetail;
import com.openport.shipmentStoppage.repository.ShipmentStoppageRepository;

@Service
public class RepositoryService {

	private final ShipmentStoppageRepository shipmentStoppageRepository;

	public RepositoryService(ShipmentStoppageRepository shipmentStoppageRepository) {
		super();
		this.shipmentStoppageRepository = shipmentStoppageRepository;
	}

	public void insertShipmentStoppageData(ShipmentStoppage shipmentStoppage) {
		shipmentStoppageRepository.save(shipmentStoppage);
	}

	public List<OrderDetail> getOrdersData() {
		List<String> getOrderList = shipmentStoppageRepository.GetOrdersData();
		List<OrderDetail> orderDetailList = getDataIntoList(getOrderList);
		if (!orderDetailList.isEmpty()) {
			return orderDetailList;
		} else {
			return null;
		}
	}

	public List<OrderDetail> getDataIntoList(List<String> orderDataList) {
		List<OrderDetail> orderDetailList = new ArrayList<>();
		for (String orderData : orderDataList) {
			OrderDetail orderDetail = new OrderDetail();
			String[] order = orderData.split(",");
			orderDetail.setOrderDetailId(order[0]);
			orderDetail.setOrderId(order[2]);
			orderDetail.setShipperId(order[3]);
			orderDetail.setStatus(order[4]);
			orderDetail.setStoppageHour(order[5]);
			orderDetail.setDispatchNumber(order[6]);
			orderDetail.setCreateDt(Timestamp.valueOf(order[1]));
			orderDetailList.add(orderDetail);
		}
		return orderDetailList;
	}

	public Boolean checkEventExist(String orderId) {
		int bool = shipmentStoppageRepository.checkEvent(orderId);
		if (bool == 1) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean checkShipmetnStoppageExist(String orderId) {
		int bool = shipmentStoppageRepository.checkShipmentStoppageExist(orderId);
		if (bool == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<OrderDetail> getEventDateWithId(String orderId) {
		List<String> getEventDtWithEventId = shipmentStoppageRepository.getEventDate(orderId);
		List<OrderDetail> list = new ArrayList<>();
		for (String data : getEventDtWithEventId) {
			OrderDetail od = new OrderDetail();
			String[] order = data.split(",");
			od.setEventDt(order[0]);
			od.setEventId(order[1]);
			list.add(od);
		}
		return list;
	}

	public String getOrderIdWhereInTransitNotExist() {
		return shipmentStoppageRepository.getOrderIdWithoutIntransitStatus();

	}

	public Boolean updateOrderWithInTransitStatus(String orderId, Timestamp updateDt) {
		try {
			shipmentStoppageRepository.updateOrderWithIntransitStatus(orderId, updateDt);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean updateShipmentStoppage(String orderId, Timestamp updateDt, int stoppageCount,
			Double totalStoppageTime, String isStopped) {
		try {
			shipmentStoppageRepository.updateShipmentStoppage(orderId, updateDt, stoppageCount, totalStoppageTime,
					isStopped);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
