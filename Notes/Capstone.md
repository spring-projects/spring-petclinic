# Code configuration

For capstone project I made some changes in this code base. Mainly in Dockerfile.

Also I learned how to use MySQL for spring petclinic - it reqiures some enviroment variables:

* MYSQL_USER
* MYSQL_PASSWORD
* MYSQL_ROOT_PASSWORD
* MYSQL_DATABASE
* MYSQL_URL

They are needed for conenction to RDS. One can use it via `docker -e MYSQL_...` or `export MYSQL_...` (for `java -jar` usage).
