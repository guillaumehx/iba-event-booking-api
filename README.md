# Event Booking API

To test the API, run `mvn clean install` via command line or directly with IntelliJ on the right panel.

It will compile the classes run the tests and generate the OpenAPI objects in the target folder.

After it, you can run the entry point `EventBookingApplication` and access the API documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html). There is basically 4 endpoints.

There is an in memory **h2 database** available at http://localhost:8080/h2-console/, make sur to use the following credentials:
- JDBC URL: `jdbc:h2:mem:booking`
- User Name: `sa`
- No password

The data loaded in this DB is the one provided in the `./data` folder. You can change it by modifying the files and running back the application.

I had fun building it because there was some tricky stuff I forgot about. We'll discuss that during the interview ;)
