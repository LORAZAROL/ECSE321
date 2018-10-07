package com.ecse321.RideShare.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.persistence.*;

import com.ecse321.RideShare.RideShareService;

@ComponentScan("com.ecse321.RideShare.*")

@Entity
@Table(name = "user_table")
public class User {
		
	@Autowired
	RideShareService service;
	
	JdbcTemplate jdbcTemplate;
	
	//JdbcTemplate jdbcTemplate;
	
	private static int idCounter = 1; //A counter that is incremented every time a User is created, so no two ID's are equal
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "userid")
	private final int userID; //User ID is final since it should never be changed
	@Column(name = "admin")
	private final Boolean isAdmin; //isAdmin is final since it should never be changed
	
	//All basic user information
	@Column(name = "firstname")
	private String firstName;
	@Column(name = "lastname")
	private String lastName;
	@Column(name = "email")
	private String email;
	@Column(name = "phone")
	private String phoneNumber;
	@Column(name = "password")
	private String password;
	@Column(name = "preferences")
	private String preferences;
	
	//Ratings are assumed to be on a 0-5 system right now, they will start at 4. Total trips are needed to average rating
	@Column(name = "passenger_rating")
	private float passengerRating;
	private int totalPassengerTrips;
	@Column(name = "driver_rating")
	private float driverRating;
	private int totalDriverTrips;
	
	}
	//Class constructor for the user
	public User(String firstName, String lastName, String email, String phoneNumber, String password, Boolean isAdmin) {
		//Set all of the info for the user given the input
		userID = idCounter;
		idCounter++;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.isAdmin = isAdmin;
		this.preferences = "";
		this.passengerRating = 4.0f;
		this.driverRating = 4.0f;
		
		//Users will start with 1 "trip" just so the average of their initial score of 4 works (their first rating shouldnt account for 100% of rating then)
		this.totalPassengerTrips = 1;
		this.totalDriverTrips = 1;
	}
	
	//All getter methods
	public int getUserID() {
		return this.userID;
	}
	public boolean getIsAdmin() {
		return this.isAdmin;
	}
	public String getFirstName() {
		return this.firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public String getEmail() {
		return this.email;
	}
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public String getPreferences() {
		return this.preferences;
	}
	public String getPassword() {
		return this.password;
	}
	public float getPassengerRating() {
		return this.passengerRating;
	}
	public float getDriverRating() {
		return this.driverRating;
	}
	
	public int getTotalPassengerTrips() {
		return totalPassengerTrips;
	}

	public void setTotalPassengerTrips(int totalPassengerTrips) {
		this.totalPassengerTrips = totalPassengerTrips;
	}

	public int getTotalDriverTrips() {
		return totalDriverTrips;
	}

	public void setTotalDriverTrips(int totalDriverTrips) {
		this.totalDriverTrips = totalDriverTrips;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPassengerRating(float passengerRating) {
		this.passengerRating = passengerRating;
	}

	public void setDriverRating(float driverRating) {
		this.driverRating = driverRating;
	}

	//All setter methods
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}
	public void updatePassengerRating(int newestRating) {
		this.totalPassengerTrips++;
		//Running average without storing a sum of all ratings
		this.passengerRating = (this.passengerRating * (this.totalPassengerTrips - 1) + newestRating)/this.totalPassengerTrips;
	}
	public void updateDriverRating(int newestRating) {
		this.totalDriverTrips++;
		//Running average without storing a sum of all ratings
		this.driverRating = (this.driverRating * (this.totalDriverTrips - 1) + newestRating)/this.totalDriverTrips;
	}

	//This can be used in test to show whether database and code communicate successfully
	@Override
	public String toString() {
		return "User [service=" + service + ", jdbcTemplate=" + jdbcTemplate + ", userID=" + userID + ", isAdmin="
				+ isAdmin + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", password=" + password + ", preferences=" + preferences
				+ ", passengerRating=" + passengerRating + ", totalPassengerTrips=" + totalPassengerTrips
				+ ", driverRating=" + driverRating + ", totalDriverTrips=" + totalDriverTrips + "]";
	}
		
	
}
