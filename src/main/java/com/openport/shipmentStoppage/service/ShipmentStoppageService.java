package com.openport.shipmentStoppage.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openport.shipmentStoppage.entity.ShipmentStoppage;
import com.openport.shipmentStoppage.model.OrderDetail;
import com.openport.shipmentStoppage.model.PingDetail;


@Service
public class ShipmentStoppageService {
	private final RepositoryService repositoryService;
	
	private final Logger logger=org.slf4j.LoggerFactory.getLogger(ShipmentStoppageService.class);
	private final String token;

	@Autowired
	public ShipmentStoppageService(RepositoryService repositoryService,@Value("${app.marketapi.token}") String token) {
		super();
		this.repositoryService = repositoryService;
		this.token=token;
		
	}	

	public void startProject() {
		System.out.println("<<--------------------------------"
		+ " SHIPMENT STOPPAGE-APPLICATION START RUNNING ---"
		+ "----------------------------->>\n");
		start();
	}
	
	public void start() {
		/*get all in-transit shipments of the day whose stoppage calculation is required */
		List<OrderDetail> getOrdersDetailData=repositoryService.getOrdersData();
		List<PingDetail> pingDetailList=new ArrayList<>();
		if(!getOrdersDetailData.isEmpty()) {
			for(OrderDetail ordersData:getOrdersDetailData) {
				String dispatchNumber=ordersData.getDispatchNumber();
				Integer shipperId=Integer.valueOf(ordersData.getShipperId());
				String orderId=ordersData.getOrderId();
				Integer stoppageHour=Integer.valueOf(ordersData.getStoppageHour());

			/* check if arriving at delivery event exists*/ 
				Boolean checkArrivingAtDeliveryEventExist=repositoryService.checkEventExist(orderId);
				if(checkArrivingAtDeliveryEventExist) {
					/*get all pings of shipment between in-transit and arriving at delivery event*/
					List<OrderDetail> getEventDtWithEventId=repositoryService.getEventDateWithId(orderId);
					if(getEventDtWithEventId.size()==2) {
						String inTransitEventDt = null;
						String arrivingAtDelvDt = null;
						for(OrderDetail od:getEventDtWithEventId) {
							if(od.getEventId().equalsIgnoreCase("7")) {
								inTransitEventDt=od.getEventDt();	
							}
							if(od.getEventId().equalsIgnoreCase("11")) {
						     	arrivingAtDelvDt=od.getEventDt();							}
							
						}
						pingDetailList=getAllPingsData(dispatchNumber,shipperId,inTransitEventDt,arrivingAtDelvDt,token);
					}
					
				}else {
					/*get all pings of shipment under in-transit status*/
					List<OrderDetail> getEventDtWithEventId=repositoryService.getEventDateWithId(orderId);
					if(getEventDtWithEventId.size()==1) {
						String inTransitEventDt = null;
						String arrivingAtDelvDt = LocalDateTime.now().toString();
						for(OrderDetail od:getEventDtWithEventId) {
							if(od.getEventId().equalsIgnoreCase("7")) {
								inTransitEventDt=od.getEventDt();	
							}	
						}
						
						pingDetailList=getAllPingsData(dispatchNumber,shipperId,inTransitEventDt,arrivingAtDelvDt,token);					
					}
					
				}
				/*Here is the stoppage calculation function*/
				calculateStoppage(pingDetailList,orderId,stoppageHour);
				
				
				
			}	
		}
		
		String orderId=repositoryService.getOrderIdWhereInTransitNotExist();
		if(orderId !=null) {
			/*Update OrderDetail with In-Transit Status*/
			Timestamp updateDate=new Timestamp(System.currentTimeMillis());
			repositoryService.updateOrderWithInTransitStatus(orderId,updateDate);	
		}	
		
	}
	
    private List<PingDetail> getAllPingsData(String dispatchNumber, Integer shipperId, String startDate, String endDate,String token) {
         List<PingDetail> pingDetailList = new ArrayList<>();
         try {
             // Constructing the URL with the hardcoded values
             String apiUrl = "https://opentmbackend.openport.com/api/order/getAllPingsWithDispatchNumberAndShipperId?"
                     + "dispatchNumber=" + dispatchNumber
                     + "&shipperId=" + shipperId
                     + "&startDate=" + startDate.replaceAll(" ", "%20")
                     + "&endDate=" + endDate.replaceAll(" ", "%20");
             
             HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
             connection.setRequestMethod("GET");
             connection.setRequestProperty("Authorization", "Bearer " + token);

             BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             StringBuilder response = new StringBuilder();
             String line;
             while ((line = reader.readLine()) != null) {
                 response.append(line);
             }
             reader.close();
             JSONObject jsonResponse = new JSONObject(response.toString());
             boolean status = jsonResponse.getBoolean("status");
             if (status) {
                 JSONArray responseArray = jsonResponse.getJSONArray("response");
                 for (int i = 0; i < responseArray.length(); i++) {
                     JSONObject data = responseArray.getJSONObject(i);
                     PingDetail pingData = new PingDetail(); // Create a new instance for each set of data
                     pingData.setLatitude(data.getString("lat"));
                     pingData.setLongitude(data.getString("lng"));
                     pingData.setPingDate(data.getString("tr_Date")); // Replace with the actual date attribute
                     pingData.setTruckNumber(data.getString("truck"));
                     pingDetailList.add(pingData);
                 }
                 return pingDetailList;
             } else {
                 System.out.println("Request failed: " + jsonResponse.getString("message"));
             }
         }catch(Exception e) {
        	 e.printStackTrace();
         }
         return pingDetailList;
     }

    
    
    public void calculateStoppage(List<PingDetail> pingDetailsList,String orderId,int stoppageHour) {
    	int j=0;
		long[] minutes= new long[50]; 
		boolean isMatched=false;
		Timestamp firstPing= null;
		Timestamp lastPing= null;
		Character isStopped='N';
		System.out.println("order id: "+orderId);
		Timestamp isStoppedPing=null;
    	for (int i=pingDetailsList.size()-1; i>=0;i--){
			System.out.println("lat: "+pingDetailsList.get(i).getLatitude()+" and lon: "+pingDetailsList.get(i).getLongitude());
			if(i!=0){ 
				Double lat=Double.valueOf(pingDetailsList.get(i).getLatitude());
				Double latBelow=Double.valueOf(pingDetailsList.get(i-1).getLatitude());
				Double lon=Double.valueOf(pingDetailsList.get(i).getLongitude());
				Double lonBelow=Double.valueOf(pingDetailsList.get(i-1).getLongitude());
				
			if(roundOff(lat) == roundOff(latBelow) &&
				roundOff(lon) == roundOff(lonBelow)) {
				if(isMatched == false) {
				if(i==pingDetailsList.size()-1) {
					isStoppedPing= Timestamp.valueOf(pingDetailsList.get(i).getPingDate());
				}	
				lastPing= Timestamp.valueOf(pingDetailsList.get(i).getPingDate());
				isMatched = true;	
				System.out.println("matched");
				}
			}
			else {
				if(isMatched==true) {
					
					firstPing=Timestamp.valueOf(pingDetailsList.get(i).getPingDate());
					if(calculateTime(firstPing,lastPing) >= stoppageHour*60) {	
					minutes[j]=calculateTime(firstPing,lastPing);
					j++;
					if(isStoppedPing==lastPing) {
						isStopped='Y';
					}
					}
					isMatched=false;
				}	
			}
			}
			else { // incase of last element
				if(isMatched==true) {
					firstPing=Timestamp.valueOf(pingDetailsList.get(i).getPingDate());
					if(calculateTime(firstPing,lastPing) >= stoppageHour*60) {
						minutes[j]=calculateTime(firstPing,lastPing);
						j++;
					}
					
				}
			}    			
		}
    	long overallTime=0;
		int counter=0;
		for (int k=0;k<minutes.length;k++) {
			if(minutes[k]> 0) {
				System.out.println("Counter: "+ ++counter);
				overallTime+=minutes[k];
			}
		}
		if(overallTime>0) {
			
		System.out.println("Total stoppage is: "+ overallTime+ " of order id: "+orderId+" and count is: "+counter);
		Boolean isExist=repositoryService.checkShipmetnStoppageExist(orderId);
		
		if(isExist==true) {
			Boolean isUpdate=repositoryService.updateShipmentStoppage(orderId,new Timestamp(System.currentTimeMillis()),counter,Double.valueOf(overallTime),String.valueOf(isStopped));
	
			if(isUpdate) {
				logger.info("Updated successfully");
			}
			
		}
		else {			
			ShipmentStoppage shipmentStoppage1=new ShipmentStoppage(Double.valueOf(overallTime),counter,String.valueOf(isStopped),Long.valueOf(orderId), new Timestamp(System.currentTimeMillis()),Timestamp.valueOf("2023-11-13 05:33:30"));

			try {
				repositoryService.insertShipmentStoppageData(shipmentStoppage1);
				logger.info(" inserted successfully");
			} catch (Exception e) {
				logger.info(" inserted Failed shipment stoppage:" +shipmentStoppage1);
			}
		}
		}
    	
    }
    
    public static long calculateTime(Timestamp firstPing, Timestamp lastPing) {
    	long milliSeconds1= firstPing.getTime();
    	long milliSeconds2= lastPing.getTime();
    	long diff=milliSeconds2 - milliSeconds1;
    	long diffMinutes= diff/(60*1000);
    	System.out.println("minutes: "+diffMinutes);
		return diffMinutes;
    }
    
    public static double roundOff(double d) {
    	DecimalFormat df = new DecimalFormat("#.####");
    	df.setRoundingMode(RoundingMode.CEILING);
    	return Double.parseDouble(df.format(d));	
    }

 

	
	
    
	
	
}
