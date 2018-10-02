# t07
Springboot backend implementation for the ride-sharing service. 

### Changelog
Sep.29, 2018: It reads the database now. 
- / returns Hello world. 
- /users returns the entire list of user_table from the DB. 
- /trips returns the entire list of trip_table from the DB. 
- Todo: Create a getter/setter class based on the current code. 

Sep.30, 2018: Added Trip and User classes
- Both classes have constructors to create an object of that type with all the user input required
- Both have getters and setters for the fields that I believe are necessary to access/alter, this can be easily expanded upon if we discover we need to
- Other methods are also included, like ones to add/remove users from a trip or to update the average driver/passenger rating of a user
- No connection has been made yet to the database or the RESTful API, just the creation of the classes

Oct.2, 2018: Added REST GET Method to create new Trip and User objects
- Each method currently takes a lot of input (all that is necessary to create a trip or user) and formats it as appropriate then creates an object from this
- This does not yet connect with the database table to create a new entry, it simply creates an instance of the object
- These methods also output a confirmation message with the name of the user or trip departure and destination locations so you know it worked
- Also added a Request method with \instructions to show how you can make a new user or trip since it requires a lot of input
