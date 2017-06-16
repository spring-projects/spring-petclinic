# GlowTouch KidClinic (built on Spring)

## Running Kidclinic locally
```bash
	git clone https://github.com/sasankglowtouch/kidclinic.git
	cd kidclinic
	./mvnw spring-boot:run
```

You can then access KidClinic here: http://localhost:8080/

## The Purpose of KidClinic
The purpose of KidClinic is to simulate the building of an enterprise application using Java and Spring. Being built off of Spring's PetClinic, KidClinic is used to understand the process of building an app in order to gain knowledge and make future development more efficient and successful.

## Current Features
- [x] Adding new parents and children
- [x] View current list of doctors and their specialities
- [x] Search through database of parents

## Upcoming Additions
| Addition | Stage of Development | Expected Development Time | Lead |
| :-------- | :-------------------- | :------------------------- | :--- |
| Login system | Research | Short Term | Emily |
| Updated child's information | Implementation | Immediate | Daniel |
| Google Map API integration | Research | Long Term | Austin |
| Add reviews section for doctors | Development | Short Term | Sasank |
| Calendar Scheduling | Research | Long Term | Sasank |

# Login System
In order to create a login system, we need to create a subfolder for the login controller. This follows [DoctorController.java](/src/main/java/org/springframework/samples/kidclinic/doctor/DoctorController.java) for the methods.

# Reviews section for Doctors
Nicely enough, this also follows the login system.

## Contact
If you are interested in the development of this app, you can contact any of the developers. You can reach us at

| Name | Email |
| :--- | :---- |
| Emily Liu | emily.liu@glowtouch.com |
| Austin Mills | austin.mills@glowtouch.com |
| Daniel Ryan | daniel.ryan@glowtouch.com |
| Sasank Vishnubhatla | sasank.vishnubhatla@glowtouch.com |
