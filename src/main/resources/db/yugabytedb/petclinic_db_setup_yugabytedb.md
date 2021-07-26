# Spring PetClinic sample application - YugabyteDB Configuration
@author Gavin Johnson

1) Sign up for Yugabyte Cloud for free [here](https://cloud.yugabyte.com/register), download and install YugabyteDB [here](https://download.yugabyte.com/), or run the `docker-compose.yugabytedb.yml` from the root of the project (if you have Docker installed locally):
   ```
   $ docker-compose -f docker-compose.yugabytedb.yml up -d
   Creating network "spring-petclinic_default" with the default driver
   Creating yugabyte ... done
   ```
   
   Use docker-compose to bring down YugabyteDB.
   ```
   $ docker-compose -f docker-compose.yugabytedb.yml down
   Stopping yugabyte ... done
   Removing yugabyte ... done
   Removing network spring-petclinic_default
   ```
2) (First time only) Create the PetClinic database and user by executing the `db/yugabytedb/user.sql` script.
   
   You can connect to the database running in the Docker container using `docker exec -it yugabyte /home/yugabyte/bin/ysqlsh -h yugabyte -U petclinic -d petclinic` (password: petclinic). You don't need to run the script there. The petclinic database and user are already set up if you use the provided `docker-compose.yugabytedb.yml`.

3) Run the app with `spring.profiles.active=yugabytedb`; `spring-boot.run.arguments="--DB_INIT=never"` for runs after the first; and `spring-boot.run.arguments="--YBDB_URL=jdbc:postgresql://[host]:[port]/petclinic?load-balance=true"` to connect to your YugabyteDB cluster (if using the Docker container, you don't need to include this).
   
   For example, from the root of the project, use: `./mvnw spring-boot:run -Dspring-boot.run.profiles=yugabytedb -Dspring-boot.run.arguments="--YBDB_URL=jdbc:postgresql://[host]:[port]/petclinic?load-balance=true"` for the first run and `./mvnw spring-boot:run -Dspring-boot.run.profiles=yugabytedb -Dspring-boot.run.arguments="--YBDB_URL=jdbc:postgresql://[host]:[port]/petclinic?load-balance=true, --DB_INIT=never"` for subsequent runs.
   
   If using the Docker container, from the root of the project, use: `./mvnw spring-boot:run -Dspring-boot.run.profiles=yugabytedb` for the first run and `./mvnw spring-boot:run -Dspring-boot.run.profiles=yugabytedb -Dspring-boot.run.arguments="--DB_INIT=never"` for subsequent runs.

**Note:** the "petclinic" database has to exist for the app to work with the JDBC URL value as it is configured by default. This condition is taken care of automatically by the docker-compose configuration provided or or running the `db/yugabytedb/user.sql` script as root.
