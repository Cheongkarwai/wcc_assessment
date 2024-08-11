1. Once you have setup your database, please run the sql script located in the classpath (/resources/sql/*.sql).
2. in application-dev.properties, modify the database connection properties.
3. please create an account first by calling the endpoint POST /api/accounts and supply a request body in json format
   eg.  request body -> {"username":"cheong","password":"123"}
4. once account is created, please call the login endpoint POST /api/authentication/login and supply a request body in json format to get the access token
   eg. request body -> {"username":"cheong","password":"123"}
5. once you get the access token, you can put it into the authorization header in a http request.
6. Then you will be able to access other endpoints since you are authenticated.

7. Location API endpoints
   - GET /api/locations query parameter: outcode:string, incode:string
   - PUT /api/locations/outcodes/{outcode} request body: {outcode: String, latitude: double, longitude: double}
   - PUT /api/locations/fullpostcodes/{postcode} request body: {postalCode: String, latitude: double, longitude: double}
   - POST /api/locations/fullpostcodes request body: {postalCode: String, latitude: double, longitude: double}
   - POST /api/locations/outcodes request body: {outcode: String, latitude: double, longitude: double}
