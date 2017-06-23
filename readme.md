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

### Adding parents and children
When viewing the parents page, users have an option of searching the parents database or adding a parent. Once a parent is chosen or added, a child can be added to that parent.

## Upcoming Additions
| Addition | Stage of Development | Expected Development Time | Lead |
| :-------- | :-------------------- | :------------------------- | :--- |
| Login system | Research | Short Term | Emily |
| Updated child's information | Implementation | Immediate | Daniel |
| Google Map API integration | Research | Long Term | Austin |
| Add reviews section for doctors | Development | Short Term | Sasank |
| Calendar Scheduling | Research | Long Term | Sasank |

### Login System
THe login system is meant to create three different views for users. There will be three categories of users: parents, nurses, and doctors. Each must login to view certain aspects of the website. Without the login system, the all the information of the parents is viewable without any security.

#### How to incorporate the login system
In order to create a login system, we need to create a subfolder for the login controller. This follows [DoctorController.java](/src/main/java/org/springframework/samples/kidclinic/doctor/DoctorController.java) for the methods.

### Reviews section for Doctors
The reviews section is meant to show the location of the doctors and include a view reviews for each doctor.

#### How to incorporate the reviews
In order the create the reviews section, we must create a controller similar to [DoctorController.java](/src/main/java/org/springframework/samples/kidclinic/doctor/DoctorController.java). Then we need to add some reviews to the database as well as locations of each doctor. Then, using [Thymeleaf](http://www.thymeleaf.org/), we can display this information in [reviews.html](/src/main/resources/templates/reviews.html).

## Contact
If you are interested in the development of this app, you can contact any of the developers. You can reach us at

| Name | Email |
| :--- | :---- |
| Emily Liu | emily.liu@glowtouch.com |
| Austin Mills | austin.mills@glowtouch.com |
| Daniel Ryan | daniel.ryan@glowtouch.com |
| Sasank Vishnubhatla | sasank.vishnubhatla@glowtouch.com |
