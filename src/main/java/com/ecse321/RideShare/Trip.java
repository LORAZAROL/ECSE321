package com.ecse321.RideShare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;

public class Trip {
	private static int tripIDCounter = 1; //Counter to ensure no two tripIDs are the same
	private final int tripID;
	private final int driverID; //This will be passed in when the trip is created and shouldn't change
	private final String driverEmail; //Driver contact info should also not change since it references the driver's user data
	private final String driverPhoneNumber;
	
	private Date departureDate; //this includes date and time
	private String departureLocation;
	private ArrayList<String> destinations = new ArrayList<String>();
	private ArrayList<Float> tripDurations = new ArrayList<Float>();
	private ArrayList<Float> prices = new ArrayList<Float>();
	
	private int availableSeats;
	private ArrayList<Integer> passengerIDList = new ArrayList<Integer>();
	private String vehicleType;
	private String licensePlate;
	private String otherComments;

	public Trip(int driverID, String driverEmail, String driverPhone, String date, String depLocation, ArrayList<String> destinations, 
			ArrayList<Float> tripDurations, ArrayList<Float> prices, int seats, String vehicleType, String licensePlate, String comments) {
		tripID = tripIDCounter;
		tripIDCounter++;
		this.driverID = driverID;
		this.driverEmail = driverEmail;
		this.driverPhoneNumber = driverPhone;
		
		this.departureDate = parseDate(date);
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
	public Date getDepartureDate() {
		return this.departureDate;
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
	
	//Methods to add a user to the trip and remove a user from a trip. These update the list of passengers' user IDs and the available seats
	public void joinTrip(int userID) {
		this.passengerIDList.add(userID);
		this.availableSeats--;
	}
	public void leaveTrip(int userID) {
		this.passengerIDList.remove(userID);
		this.availableSeats++;
	}
	
	//Likely don't want drivers changing a lot of the info on their trip, because you don't want existing passengers blind-sided
	//by a sudden change to price or destination, etc. So not many things need to be set after the fact
	public void setOtherComments(String comments) {
		this.otherComments = comments;
	}
	
	//Method that takes a String formatted in the manner shown below and turns it into a date format object
	private static Date parseDate(String date) {  
		try {
	    Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);  
	    return date1;
		} catch (ParseException e) {
            return null;
        }
	}

}
