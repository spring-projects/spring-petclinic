================================================================================
===        Spring PetClinic sample application - Database Configuration      ===
================================================================================

@author Costin Leau

--------------------------------------------------------------------------------

In its default configuration, Petclinic uses an in-memory database (HSQLDB) which
gets populated at startup with data. Since there is no persistent support, once
the application is destroyed, so is the database.
If a persistent database configuration is chosen, make sure to change the datasource
inside the relevant application-*.xml so that the schema and the data do not get 
inserted each time the application is started. Additionally, update the jdbc.properties
file to reflect your change.