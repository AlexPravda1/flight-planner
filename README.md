# Welcome to Flight reporting application!

<img src="https://media.cntraveler.com/photos/5be477cf90e25c2d60bee0f1/master/w_1600%2Cc_limit/CNT_Intel_Plane_Landing_TM-Detwiler_110718.jpg" width="400" alt="airplane">

## The goal of this project is:
* Create a Java application using Spring and Hibernate
* Provide Stateless JWT authentication
* Provide overview of the flights based on Flight Planning system

~~~
Project is deployed via Heroku https://leon-planner.herokuapp.com/
~~~
This is a web project, which could be characterized as a data gathering application. <BR>
The project is based on the principles of SOLID and three-tier software architecture.<BR>
Besides, such frameworks as Spring Web, Spring REST, Spring Security, Hibernate have been used to accomplish the task.<BR>
Authentication and authorization are also represented as a part of a project in a form of JWT tokens stored in HTTP Only Cookie.<BR>
HTTP requests can be sent in JSON format and are stored in a database.<BR>

## Technologies that were used to create the service:
* Java 11
* MySQL
* Hibernate
* Spring (Core, WEB, Security)
* Apache Tomcat (to run app locally)
* JWT Tokens (encrypted, valid for 7 days) are stored in Cookies
* JSP page to represent the result
* JSON mapping into POJO classes via DozerMapper
* Maven Checkstyle Plugin

## Overview
#### Project has multiple endpoints with user and admin access
POST: `/register` (to create a user) - ALL Roles (but Airline AccessKey is required for proper results display) <br/>
GET: `/profile` (to get information about the user and list of availbale aircraft) - ALL Roles <br/>
GET: `/flights` (to get list of the flights) - ALL Roles <br/>
Can be parametrized with: <br/>
`daysRange` - between 0 and 90 days, to filter list for specific date range from today till indicated amount of days. <br/> 
`registration` - string format, to filter list for specific aircraft. <br/>
`hasNotes` - boolean, filter list for flights with remarks only. <br/>
`hasFiles` - boolean, filter list for flights with files attached only. <br/>

GET: `/users` (to get list of available users in the system) - ADMIN Role <br/>
GET: `/users/by-email` (to get specific user info) - ADMIN Role <br/>

#### Example how to add data into the application (using for example Postman)
{"email":"bob@gmail.com", "password":"bobsPass", "repeatPassword":"bobsPass", "name":"Bob", "surname":"Smith"}  POST: /register <br/>

## How to start cinema service locally:
1. Install and configure Apache Tomcat (recommended version 9.0.50)
2. Install and configure and create a schema in MySQL
3. Fork and clone this project
4. To connect to database in application you need change configuration information
   in the file from `/resources/application.properties` to the ones you specified when installing MySQL
5. DB properties are configured as ENV variables or can be placed into 'application.properties' file:
~~~
    db.driver= YOUR_DB_DRIVER
    db.password= YOUR_DB_PASSWORD
    db.url= YOUR_DB_URL
    db.username= YOUR_DB_USERNAME
    security.cipher.secret-key= SECRET_WORD_FOR_COOKIE_ENCRYPTION
    security.jwt.cookie.token= TOKEN_NAME (JWT token will be saved with this name in the Cookie)
    security.jwt.token.secret-key= JWT_TOKEN_SECRET_WORD
~~~
5. Run this project using Tomcat local server

