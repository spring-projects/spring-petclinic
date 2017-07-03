# Splice Machine Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-projects/spring-petclinic.png?branch=master)](https://travis-ci.org/spring-projects/spring-petclinic/)

## Understanding the Spring Petclinic application with a few diagrams
<a href="https://speakerdeck.com/michaelisvy/spring-petclinic-sample-application">See the presentation here</a>

## Running petclinic locally
```
	git clone https://github.com/splicemachine/spring-petclinic.git
	cd spring-petclinic
	./mvnw spring-boot:run
```

You can then access petclinic here: http://localhost:8080/

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

## In case you find a bug/suggested improvement for Spring Petclinic
Our issue tracker is available here: https://github.com/splicemachine/spring-petclinic/issues


## Database configuration

In its default configuration, Petclinic uses an in-memory database (HSQLDB) which
gets populated at startup with data. 

You could start a Splice Machine database running locally by editing application.properties:

```
database=splicemachine
spring.datasource.schema=classpath*:db/${database}/schema.sql
spring.datasource.data=classpath*:db/${database}/data.sql


spring.datasource.url=jdbc:splice://localhost:1527/splicedb;user=splice;password=admin
spring.datasource.username=splice
spring.datasource.password=admin
spring.datasource.driver-class-name=com.splicemachine.db.jdbc.ClientDriver
spring.jpa.database-platform=com.splicemachine.SpliceMachineDialect
```
See our [tutorials](https://www.splicemachine.com/tutorial/connecting-programmatically/) to connect to our ODBC driver to allow Splice Machine to connect any other database or business tools that needs access to your database

## Working with Petclinic in Eclipse/STS

### prerequisites
The following items should be installed in your system:
* Maven 3 (http://www.sonatype.com/books/mvnref-book/reference/installation.html)
* git command line tool (https://help.github.com/articles/set-up-git)
* Eclipse with the m2e plugin (m2e is installed by default when using the STS (http://www.springsource.org/sts) distribution of Eclipse)

Note: when m2e is available, there is an m2 icon in Help -> About dialog.
If m2e is not there, just follow the install process here: http://eclipse.org/m2e/download/


### Steps:

1) In the command line
```
git clone https://github.com/spring-projects/spring-petclinic.git
```
2) Inside Eclipse
```
File -> Import -> Maven -> Existing Maven project
```
