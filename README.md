# springboot-multi-tenant-with-security
This application implemnents the schema per tenant approach in a multi-tenant scenario.

# Steps to test this application:

* Execute the <code>DB_Scripts.sql</code> file for each tenant. Create database instance and tables by using this file.
* Run the Main application.
* Use the below Rest end point to create student under each tenant (database)
* Import the Rest API collection (<code>api-collection.postman.json</code>) in POSTMAN and use the *login* api for authentication.
* Once this login is successful, use the *add user* api to add a student in the database.

* A note on the tenants:
  * If we login by using "user/him@123", then when we save the student details using add-user api, tenant_01 would be used.
  * If we login by using "admin/admin@123", then when we save the student details using add-user api, tenant_02 would be used.


* How it works:
  * The tenants are configured as the User Roles. So, when we login successfully, the user role notifies the tenant id.
  * As part of successful login, a JWT token is set in the response. The tenant ID is encoded in this token.
  * Now, when we try to access other resources/services, we pass this token in the header and then this token is validated and the tenant ID is extracted from this token and set in the Tenant context (which is a threadlocal object).
  * By doing so, the tenant information is now available in the thread context and hence the correct tenant is used for database operations.
  
  
  
