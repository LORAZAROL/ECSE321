package com.ecse321.RideShare.model;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class Trip {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static int tripIDCounter = 1; //Counter to ensure no two tripIDs are the same
	private final int tripID;
	private final int driverID; //This will be passed in when the trip is created and shouldn't change
	private final String driverEmail; //Driver contact info should also not change since it references the driver's user data
	private final String driverPhoneNumber;
	
	private LocalDate departureDate; //this is just date part
	private LocalTime departureTime; //this is just the time part
	private String departureLocation;
	private ArrayList<String> destinations = new ArrayList<String>();
	private ArrayList<Float> tripDurations = new ArrayList<Float>();
	private ArrayList<Float> prices = new ArrayList<Float>();
	
	private int availableSeats;
	private ArrayList<Integer> passengerIDList = new ArrayList<Integer>();
	private String vehicleType;
	private String licensePlate;
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
	public LocalDate getDepartureDate() {
		return this.departureDate;
	}
	public LocalTime getDepartureTime() {
		return this.departureTime;
	}
	public String getDepartureLocation() { 
		return this.departureLocation;
	}
	public ArrayList<String> getDestination() {
		return this.destinations;
	}
	public ArrayList<Float> getPrices() {
		return this.prices;
	}
	public int getSeats() {
		return this.availableSeats;
	}
	
	/*
	//Methods to add a user to the trip and remove a user from a trip. These update the list of passengers' user IDs and the available seats
	public void joinTrip(int userID) {
		this.passengerIDList.add(userID);
		this.availableSeats--;
	}
	public void leaveTrip(int userID) {
		this.passengerIDList.remove(userID);
		this.availableSeats++;
	}
	*/
	
	//Likely don't want drivers changing a lot of the info on their trip, because you don't want existing passengers blind-sided
	//by a sudden change to price or destination, etc. So not many things need to be set after the fact
	public void setOtherComments(String comments) {
		this.otherComments = comments;
	}
	
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
	
	public void addToDatabase() {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("INSERT INTO trip_table (trip_id, driver_id, departure_date, departure_time, departure_location, durations, destinations, seats_available, passenger_id, prices, vehicle_type, licence_plate, contact_no, comments)"
				+ "VALUES (" + this.tripID + ", " + this.driverID + ", '" + this.departureDate + "', '" + this.departureTime + "', '" + this.departureLocation + "', '{" + arrayListToString(this.tripDurations) + "}', '{"
				+ arrayListToString(this.destinations) + "}', " + this.availableSeats + ", '{" + arrayListToString(this.passengerIDList) + "}', '{" + arrayListToString(this.prices) + "}', '" + this.vehicleType + "', '" + this.licensePlate + "', '" + this.driverPhoneNumber + "', '" + this.otherComments + "')");
	}
	
	//Used to turn the elements of an ArrayList into a String separated by commas (useful for addition to database table)
	private String arrayListToString(ArrayList list) {
		String string = "";
		
		for (int i=0; i<list.size(); i++) {
			string = string + list.get(i);
			if (i != (list.size() - 1)) {
				string = string + ", ";
			}
		}
		
		return string;
	}
	
}
