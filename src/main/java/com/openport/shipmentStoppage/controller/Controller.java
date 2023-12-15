package com.openport.shipmentStoppage.controller;

import java.util.concurrent.TimeUnit;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.openport.shipmentStoppage.service.ShipmentStoppageService;

/**
 * @author Farhan Rana
 *
 * Running jobs for Shipment Stoppage
 */
@RestController
public class Controller {
	
	
	private final ShipmentStoppageService shipmentStoppageService;

	public Controller(ShipmentStoppageService shipmentStoppageService) {
		super();
		this.shipmentStoppageService = shipmentStoppageService;
		shipmentStoppageService.startProject();
	}
	
//	@Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES) // Run the job every 30 minutes
//	public void parseNewShabbirOrders(){
//		shipmentStoppageService.startProject();
//	}
	
	

}
