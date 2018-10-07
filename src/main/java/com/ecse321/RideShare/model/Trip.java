package com.ecse321.RideShare.model;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ecse321.RideShare.RideShareService;

@ComponentScan("com.ecse321.RideShare.*")

@Entity
@Table(name = "trip_table")
public class Trip {
	@Autowired
	RideShareService service;
	
	JdbcTemplate jdbcTemplate;
	
	private static int tripIDCounter = 1; //Counter to ensure no two tripIDs are the same
	@Id
	@Column(name = "trip_id")
	private final int tripID;
	@Column(name = "driver_id")
	private final int driverID; //This will be passed in when the trip is created and shouldn't change
	private final String driverEmail; //Driver contact info should also not change since it references the driver's user data
	private final String driverPhoneNumber;
	
	@Column(name = "departure_date")
	private LocalDate departureDate; //this is just date part
	@Column(name = "departure_time")
	private LocalTime departureTime; //this is just the time part
	@Column(name = "departure_location")
	private String departureLocation;
	
	//dependencies need to be confirmed
	@Type(type = "string-array")
	@Column(name = "destinations",columnDefinition="text[]")
	private String[] destinations1;
	@Type(type = "float-array")
	@Column(name = "durations",columnDefinition="text[]")
	private float[] tripDurations1;
	@Type(type = "float-array")
	@Column(name = "prices",columnDefinition="text[]")
	private float[] prices1;
	@Type(type = "int-array")
	@Column(name = "passenger_id")
	private int[] pId;
	
	//cast above arrays into arraylist
	private ArrayList<String> destinations = new ArrayList<String>(Arrays.asList(destinations1));
	private ArrayList<Float> tripDurations = toArrayList(tripDurations1);
	private ArrayList<Float> prices = toArrayList(prices1);
	private ArrayList<Integer> passengerIDList = toArrayList(pId);
	
	@Column(name = "seats_available")
	private int availableSeats;
	@Column(name = "vehicle_type")
	private String vehicleType;
	@Column(name = "license_plate")
	private String licensePlate;
	@Column(name = "comments")
	private String otherComments;

	public Trip(int driverID, String driverEmail, String driverPhone, String date, String time, String depLocation, ArrayList<String> destinations, 
			ArrayList<Float> tripDurations, ArrayList<Float> prices, int seats, String vehicleType, String licensePlate, String comments) {
		tripID = tripIDCounter;
		tripIDCounter++;
		this.driverID = driverID;
		this.driverEmail = driverEmail;
		this.driverPhoneNumber = driverPhone;
		
		this.departureDate = LocalDate.parse(date);
		this.departureTime = LocalTime.parse(time);
		this.departureLocation = depLocation;
		this.destinations = destinations;
		this.tripDurations = tripDurations;
		this.prices = prices;
		
		this.availableSeats = seats;
		this.vehicleType = vehicleType;
		this.licensePlate = licensePlate;
		this.otherComments = comments;

	}
	
	//Get methods for typical fields that might be searched for
	public int getTripID() {
		return this.tripID;
	}
	public int getDriverID() {
		return this.driverID;
	}
	public LocalDate getDepartureDate() {
		return this.departureDate;
	}
	public LocalTime getDepartureTime() {
		return this.departureTime;
	}
	public String getDepartureLocation() { 
		return this.departureLocation;
	}
	public ArrayList<Float> getTripDurations() {
		return this.tripDurations;
	}
	public ArrayList<String> getDestination() {
		return this.destinations;
	}
	public ArrayList<Integer> getPassengerIDList() {
		return this.passengerIDList;
	}
	public ArrayList<Float> getPrices() {
		return this.prices;
	}
	public int getSeats() {
		return this.availableSeats;
	}
	public String getVehicleType() {
		return this.vehicleType;
	}
	public String getLicencePlate() {
		return this.licensePlate;
	}
	public String getDriverPhone() {
		return this.driverPhoneNumber;
	}
	public String getComments() {
		return this.otherComments;
	}
	
	//Likely don't want drivers changing a lot of the info on their trip, because you don't want existing passengers blind-sided
	//by a sudden change to price or destination, etc. So not many things need to be set after the fact
	public void setOtherComments(String comments) {
		this.otherComments = comments;
	}
<<<<<<< HEAD
	
	//Method that takes a String formatted in the manner shown below and turns it into a date format object
	/*
	private static Date parseDate(String date) { 
		//use sql.date instead of util.date
		try {
	    java.util.Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);  
	    java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
	    	return sqlDate;
		} catch (ParseException e) {
            return null;
        }
	}
	*/
	
	//convert float array to arraylist
	private ArrayList<Float> toArrayList(float[] arr){
		ArrayList<Float> tmp = new ArrayList<Float>();
		for(int i=0; i<arr.length;i++) {
			tmp.add(arr[i]);
		}
		return tmp;
	}
	//convert int array to arraylist
	private ArrayList<Integer> toArrayList(int[] arr){
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for(int i=0; i<arr.length;i++) {
			tmp.add(arr[i]);
		}
		return tmp;
	}
	
=======
>>>>>>> 6b46d751b7dc0f7a0c7e6f54ad59ceb9b702c799
}
