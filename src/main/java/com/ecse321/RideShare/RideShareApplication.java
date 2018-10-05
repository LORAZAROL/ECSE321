package com.ecse321.RideShare;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecse321.RideShare.model.User;
import com.ecse321.RideShare.model.Trip;


@SpringBootApplication
@RestController

public class RideShareApplication {
	private static final Logger LOG = LoggerFactory.getLogger(RideShareApplication.class);
  
	public static void main(String[] args) {
		SpringApplication.run(RideShareApplication.class, args);
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(path="/")
	public String greeting() {
		return "Hello, world! ";
	}
	
	//Created a new path just to provide the instructions on how to create a new user or trip
	@RequestMapping(path="/instructions")
	public String instructions() {
		return "Create a new user with a POST request: /users/create \n"
        		+ "Create a new trip with a POST request: /trips/create \n \n"
        		+ "Delete a user with a DELETE request: /users \n"
        		+ "Delete a trip with a DELETE request: /trips \n \n"
        		+ "See all users with a GET request: /users \n"
        		+ "See all trips with a GET request: /trips \n"
        		+ "Search for users with a POST request: /users/search \n"
        		+ "Search for trips with a POST request: /trips/search";
		}
	
/*
 * API for Requesting Data
 */
	/* 
	 * For user data (DB: 'user_table')
	 */
	
	// returns the full list of users
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public String user_list (ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String userid ) {	
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("select * from user_table");
		return list.toString();
    }
	
	// search user_table based on id or name
	@RequestMapping(path="/users/search", method=RequestMethod.POST)
	public String user_search (ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String userid, @RequestParam(name="keyword", defaultValue= "") String keyword) {
		if (userid.isEmpty() == false) {
			List<Map<String,Object>> list;
			list = jdbcTemplate.queryForList("select * from user_table where userid = ?", Integer.parseInt(userid));
			return list.toString();
		}
		else if (keyword.isEmpty() == false) {
			List<Map<String,Object>> list;
			
	    		// this is security-wise terribly terribly BAD, but let it be for now
			String keywords[] = keyword.split(" ");	// split using space. 
			
			// only checks for first two elements
			if (keywords[0].isEmpty() == false) {
				keyword = "firstname ='" + keywords[0] + "' OR lastname = '" + keywords[0] + "'";
			}
			if (keywords[1].isEmpty() == false) {
				keyword = keyword + " OR firstname ='" + keywords[1] + "' OR lastname = '" + keywords[1] + "' ";
			}
			
			String query = "select * from user_table WHERE " + keyword;
			list = jdbcTemplate.queryForList(query);
			return list.toString();
		}
		else {
			return "Usage: Send a POST request to \"/users/search?id={id}\" or \"/users/search?keyword={name}\"";
		}
	}
	
	
	/* 
	 * For trip data (DB: 'trip_table')
	 */
	
	// return the list of trips
	@RequestMapping(path="/trips", method=RequestMethod.GET)
	public String trip() {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("select * from trip_table");
		return list.toString();
	}
       
	// searching trips based on queries
	@RequestMapping(path="/trips/search", method=RequestMethod.POST)
	public String trip_search(ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String tripid, @RequestParam(name="dep", defaultValue="") String departure, 
			@RequestParam(name="dest", defaultValue="") String destination, @RequestParam(name="date", defaultValue="") String date, 
			@RequestParam(name="seats", defaultValue="") String seats) {
		if (tripid.isEmpty() == false) {
			List<Map<String,Object>> list;
			list = jdbcTemplate.queryForList("select * from trip_table where trip_id = ?", Integer.parseInt(tripid));
			return list.toString();
		}
		else if (departure.isEmpty() == false) {
			if (departure.isEmpty() == false) { departure = "departure_location='" + departure + "'"; } else { return "Error. "; }
    			if (destination.isEmpty() == false) { destination = "AND destinations='" + destination + "'"; }
    			if (date.isEmpty() == false) { date = "AND departure_date='" + date + "'"; }
    			if (seats.isEmpty() == false) { date = "AND seats_available>='" + seats + "'"; }
    		
    			List<Map<String,Object>> list;
    			// this is security-wise terribly terribly BAD, but leave as is for now
    			String query = "select * from trip_table WHERE " + departure + destination + date;
    			list = jdbcTemplate.queryForList(query);
    			return list.toString();
		}
		else {
			return "Usage: Send a POST request to \"/trips/search?dep={departure_location}&dest={destination}&date={departure_date}&seats={seats_required}\"";
		}
    		
    }
    
    /*
     * API for Writing into Database
     */
	
    //Creates a new user by taking all of the below-specified inputs
    @PostMapping("/users/create")
	public String createUser(@RequestParam(name="firstName", defaultValue= "") String firstName, @RequestParam(name="lastName", defaultValue="") String lastName, @RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="phoneNumber", defaultValue="") String phoneNumber, @RequestParam(name="password", defaultValue="") String password, @RequestParam(name="isAdmin", defaultValue="") String isAdminInt) {
    	
    	if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()&& !password.isEmpty()) {
    		//Currently accepting an int for isAdmin and turning it into a boolean afterwards
        	Boolean isAdmin = false;
        	if (isAdminInt == "true") {
    			isAdmin = true;
    		}
        	
        	//Creates the object and returns a confirmation message that it worked
        	User newUser = new User (firstName, lastName, email, phoneNumber, password, isAdmin);
        	newUser.addToDatabase();
    		String confirmationText = "New User: " + newUser.getFirstName() + " " + newUser.getLastName() + " created successfully!";
    		return confirmationText;
    	}
    	else {
    		return "Please enter all mandatory user information (First name, last name, email and password are required) \n"
    				+ "\"Usage: Send a POST request to \"/users/create?firstName={First Name}&lastName={Last Name}&email={Email Address}&phoneNumber={Phone Number}&password={Password}\"";
    	}
	}
    
    //Creates a new trip by taking all of the below-specified inputs
    @PostMapping("/trips/create")
    public String createTrip(@RequestParam(name="driverID", defaultValue="") String driverIDString, @RequestParam(name="driverEmail", defaultValue="") String driverEmail, @RequestParam(name="driverPhone", defaultValue="") String driverPhone,
    		@RequestParam(name="date", defaultValue="") String date, @RequestParam(name="depTime", defaultValue="") String time, @RequestParam(name="depLocation", defaultValue="") String depLocation, @RequestParam(name="destinations", defaultValue="") String destinations, 
    		@RequestParam(name="tripDurations", defaultValue="") String tripDurations, @RequestParam(name="prices", defaultValue="") String prices, @RequestParam(name="seats", defaultValue="") String seatsString, 
    		@RequestParam(name="vehicleType", defaultValue="") String vehicleType, @RequestParam(name="licensePlate", defaultValue="") String licensePlate, @RequestParam(name="comments", defaultValue="") String comments) {
    	
    	//Driver ID and seats are both input as strings but need to be converted to int
    	int driverID;
    	int seats;
    	
    	if (!driverIDString.isEmpty() && !seatsString.isEmpty()) {
    		driverID = Integer.parseInt(driverIDString);
        	seats = Integer.parseInt(seatsString);
        	
        	if (driverID != 0 && !driverEmail.isEmpty() && !date.isEmpty() && !depLocation.isEmpty() && !destinations.isEmpty() && !prices.isEmpty() && seats != 0) {
    	    	if (!isValidFormat(date, "yyyy-MM-dd")) {
    	    		return "Please enter the Departure Date in the following format: yyyy-MM-dd";
    	    	}
    	    	if (!isValidFormat(time, "HH:mm:ss")) {
    	    		return "Please enter the Departure Time in the following format: HH:mm:ss";
    	    	}
    	    	else {
    	    		//Array Lists receive their data as pure string (with commas to separate) from the URL and use below methods to create a separated list from that
    		    	ArrayList<String> destinationList = new ArrayList<String>(Arrays.asList(separateByComma(destinations)));
    		    	
    		    	//Duration and Prices need to be in float format but are input as strings, so they are converted first
    		    	String[] strDuration = separateByComma(tripDurations);
    		    	Float[] floatDuration = stringToFloatArray(strDuration);    	
    		    	ArrayList<Float> tripDurationList = new ArrayList<Float>(Arrays.asList(floatDuration));
    		    	
    		    	String[] strPrices = separateByComma(prices);
    		    	Float[] floatPrices = stringToFloatArray(strPrices);    	
    		    	ArrayList<Float> pricesList = new ArrayList<Float>(Arrays.asList(floatPrices));
    		    	
    		    	//Creates the object and returns a confirmation message that it worked
    		    	Trip newTrip = new Trip (driverID, driverEmail, driverPhone, date, time, depLocation, destinationList, tripDurationList, pricesList, seats, vehicleType, licensePlate, comments);
    		    	newTrip.addToDatabase();
    		    	String confirmationText = "New Trip from " + newTrip.getDepartureLocation() + " to " + String.join(", then ", newTrip.getDestination()) + " created successfully!";
    		    	return confirmationText;
    	    	}
        	}
        	else {
            	return "Please enter all mandatory trip information (Driver ID, driver email, date, departure location, destination(s), price(s) and available seats) \n"
    			+ "\"Usage: Send a POST request to \"/trips/create?driverID={Driver ID}&driverEmail={Driver Email}&driverPhone={Driver Phone Number}&date={yyyy-mm-dd}&depTime={HH:MM:SS}&depLocation={Departure Location}&"
    			+ "destinations={Destination1, Destination2, etc}&tripDurations={Duration1, Duration2, etc}&prices={Price1, Price2, etc}&seats={Available Seats}&vehicleType={Vehicle Type}&"
    			+ "licensePlate={License Plate Number}&comments={Additional Comments}\"";
        	}
    	}

    	else {
        	return "Please enter all mandatory trip information (Driver ID, driver email, date, departure location, destination(s), price(s) and available seats) \n"
			+ "\"Usage: Send a POST request to \"/trips/create?driverID={Driver ID}&driverEmail={Driver Email}&driverPhone={Driver Phone Number}&date={yyyy-mm-dd}&depTime={HH:MM:SS}&depLocation={Departure Location}&"
			+ "destinations={Destination1, Destination2, etc}&tripDurations={Duration1, Duration2, etc}&prices={Price1, Price2, etc}&seats={Available Seats}&vehicleType={Vehicle Type}&"
			+ "licensePlate={License Plate Number}&comments={Additional Comments}\"";
    	}
    }
    
    //Adds a user to a specified trip
    @PostMapping("/trips/join")
    public String joinTrip (ModelMap modelMap, @RequestParam(name="userID", defaultValue= "") String userID, @RequestParam(name="tripID", defaultValue= "") String tripID) {
    	if (!userID.isEmpty() && !tripID.isEmpty()) {
    		int userIDInt = Integer.parseInt(userID);
    		int tripIDInt = Integer.parseInt(tripID);
    		List<Map<String,Object>> list;
    		list = jdbcTemplate.queryForList("UPDATE trip_table SET passenger_id = array_append(passenger_id,'" + userIDInt + "'), seats_available = (seats_available - 1) WHERE trip_id=? and seats_available>0", tripIDInt);
    		return list.toString();
    	}
    	else {
    		return "Usage: Send a POST request to \"/trips/join?userID={Your User ID}&tripID={Desired Trip ID}\"";
    	}
    	
    }
    
    //Removes a user from a specified trip
    @PostMapping("/trips/leave")
    public String leaveTrip (ModelMap modelMap, @RequestParam(name="userID", defaultValue= "") String userID, @RequestParam(name="tripID", defaultValue= "") String tripID) {
    	if (!userID.isEmpty() && !tripID.isEmpty()) {
    		int userIDInt = Integer.parseInt(userID);
    		int tripIDInt = Integer.parseInt(tripID);
    		List<Map<String,Object>> list;
    		list = jdbcTemplate.queryForList("UPDATE trip_table SET passenger_id = array_remove(passenger_id,'" + userIDInt + "'), seats_available = (seats_available + 1) WHERE trip_id=?", tripIDInt);
    		return null;
    	}
    	else {
    		return "Usage: Send a POST request to \"/trips/leave?userID={Your User ID}&tripID={Desired Trip ID}\"";
    	}
    }
    
    
    
    /*
     * API for Deleting Database Rows
     */
    
    //Used to delete a trip from the trip_table using the trip ID
    @DeleteMapping(path="/trips")
	public String deleteTrip(ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String tripID) {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("DELETE FROM trip_table WHERE trip_id = ?", Integer.parseInt(tripID));
		return "Trip with a trip ID of: " + tripID + " was deleted.";
	}
    //Used to delete a user from the user_table using the user ID
    @DeleteMapping(path="/users")
	public String deleteUser(ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String userID) {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("DELETE FROM user_table WHERE userid = ?", Integer.parseInt(userID));
		return "User with a user ID of: " + userID + " was deleted.";
	}
    
    /*
     * Additional methods used to manipulate the data
     */
    
    //Simple method to take a string and separate it by commas into an array (for user input)
    public String[] separateByComma(String input) {
    	String[] values = input.split("\\s*,\\s*");

    	return values;
    }
    
    //Simple method that takes a string array and converts each value into a float (for user input)
    public Float[] stringToFloatArray(String[] strArray) {
    	Float[] floatValues = new Float[strArray.length];
		for (int i = 0; i<strArray.length; i++) {
			floatValues[i] = Float.parseFloat(strArray[i]);
		}
		return floatValues;
    }
    
    //Checks if the user input the date or time in a viable format, returns true if so, false if not
    public static boolean isValidFormat(String value, String format) {
    	Locale locale = Locale.ENGLISH;
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }
}

