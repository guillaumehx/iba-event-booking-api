# Event Booking API

To test the API, run **mvn clean install** via command line or directly with IntelliJ on the right panel.

It will compile the classes run the tests and generate the OpenAPI objects in the target folder.

After it, you can run the entry point **EventBookingApplication** and access the API documentation at http://localhost:8080/swagger-ui.html

There is an **h2 database**, the data loaded in it is the one provided in the ./data folder. You can change it by modifying the files and running back the application.

I had fun building it because there was some tricky stuff I forgot about ;)