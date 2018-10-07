# t07
Springboot backend implementation for the ride-sharing service. 

HOW TO USE:
1. Using a REST client, send a GET request to "/instructions" to be provided with a list of actions
2. Sending the associated request for any of those instructions will provide you with the required parameters that must be entered (if necessary)

### Changelog
Oct. 7, 2018: Fixed unintended cases for Join/Leave
- Previously you could Join a trip even if your passenger ID was already on the list, adding you twice and taking another seat
- Now it checks if your ID is already on the list and if you are, then you aren't added
- You can now only Leave a trip if your ID is already on the list, or else previously it would reduce the available seats even though no one was taken off the list
- Added HOW TO USE at the top of the README

Oct. 6, 2018: Added JUnit tests for User and Trip classes
- 4 JUnit tests were added
- One to test some of the getters and setters of the User class
- One to test some of the getters and setters of the Trip class
- One to test the update driver/passenger rating methods of the User class
- One to test the isValidFormat function in Controller properly works with date formats

Oct. 6, 2018: Bug fixing for most API Endpoints
- Altered the user search to work with any number of names separated by spaces
- Fixed the trip search so that it properly searches for the destination in the array within the table column
- Changed the method in the service class used by the delete endpoints to be sqlInsert so that it runs with the update commmand and doesn't return seemingly an error when it is working fine
- Changed search methods and create methods to use .toLowerCase() for certain strings so that data isn't stored in the database with a random collection of upper/lower case if the user does so, and allows for searches that wont fail
- Cleaned up leftover commented code that has long since been abandoned
- Added further comments to sections for readability

Oct. 5, 2018: Fixing DB issue of the updates on Oct. 4. 
- Added service class to handle the DB - so that injection does not fail.  

Oct. 4, 2018: Joining and Leaving Trips:
- Added two methods that use POST mapping to allow the user to add or join trips
- They take 2 inputs: userID and tripID
- The associated userID will be added or removed from the trip associated with the tripID (depending on the method you use)
- The available seats on the trip also change according to joining/leaving
- Was not able to test functionality of this yet, waiting on reconfigure of trip_table column's data type (See Issue #13)

Oct.4, 2018: Adding Users/Trips and Deleting Users/Trips:
- Added a public method in the user and trip classes that takes all of their data, formulates a SQL query and inserts the row into the associated table (there appears to still be some errors in this portion, getting a NULL Pointer Exception)
- Added methods in the main application that delete a user or trip from the associated table based on ID
- Reconfigured the Trip class to use LocalDate and LocalTime instead of previous setup since it works natively with PostgreSQL

Oct.3, 2018: Search component: 
- Changed the URI for searching trips to "/trips/search". Returns usage if no queries given. 
Usage: Send a POST request to "/trips/search?dep={departure_location}&dest={destination}&date={departure_date}&seats={seats_required}"
- Added the user search to "/users/search". Returns usage if no queries given. 
Queries: id (userid), keyword (firstname, lastname, or both but separated using space)
Usage: Send a POST request to "/users/search?id={id}" or "/users/search?keyword={name}"

Oct.2, 2018: Added REST GET Method to create new Trip and User objects
- Each method currently takes a lot of input (all that is necessary to create a trip or user) and formats it as appropriate then creates an object from this
- This does not yet connect with the database table to create a new entry, it simply creates an instance of the object
- These methods also output a confirmation message with the name of the user or trip departure and destination locations so you know it worked
- Also added a Request method with \instructions to show how you can make a new user or trip since it requires a lot of input

Oct.1, 2018: Added Persistence, service, model and controller packages
Change package structures:
add package model, controller, persistence, service packages inside RideShare package. Model is consisted of trip and user class. This modification is made to reduce the amount of reload when importing these two classes.
Add "Passenger ID" field in trip constructor
Add Persistence class:
method to connect database on Heroku to code
method to return newly changed trip
this is not completed yet
Change import package "util.date" to "sql.date", in the case of exchanging data "date" with PostgreSQL
Lots of work isn't finished yet, but I still want to make one commit. If you read and find any problem please tell me

Sep.30, 2018: Added Trip and User classes
- Both classes have constructors to create an object of that type with all the user input required
- Both have getters and setters for the fields that I believe are necessary to access/alter, this can be easily expanded upon if we discover we need to
- Other methods are also included, like ones to add/remove users from a trip or to update the average driver/passenger rating of a user
- No connection has been made yet to the database or the RESTful API, just the creation of the classes

Sep.29, 2018: It reads the database now. 
- / returns Hello world. 
- /users returns the entire list of user_table from the DB. 
- /trips returns the entire list of trip_table from the DB. 
- Todo: Create a getter/setter class based on the current code. 
