# Welcome to Flight reporting application!

<img src="https://media.cntraveler.com/photos/5be477cf90e25c2d60bee0f1/master/w_1600%2Cc_limit/CNT_Intel_Plane_Landing_TM-Detwiler_110718.jpg" width="400" alt="airplane">

## The goal of this project is:
* Create a Java application using Spring and Hibernate
* Provide overview of the flights based on Flight Planning system

This is a web project, which could be characterized as a simple data gathering application. <BR>
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
* JWT Tokens
* Encryption and Decryption with SecurityCipher class
* JSP page to represent the result
* JSON mapping into POJO classes via DozerMapper
* Maven Checkstyle Plugin

## Overview
#### Project has multiple endpoints with user and admin access
POST: `/register` (to create a user) - ALL <br/>
POST: `/cinema-halls` (to create a cinema hall) - ADMIN <br/>
POST: `/movies` (to create a movie) - ADMIN <br/>
POST: `/movie-sessions` (to create a movie sessions) - ADMIN <br/>
POST: `/orders/complete` (to create an order for current user) - USER <br/>
PUT: `/movie-sessions/{id}` (to update a movie session) - ADMIN <br/>
PUT: `/shopping-carts/movie-sessions` (to add movie session to shopping cart) - USER <br/>
DELETE: `/movie-sessions/{id}` (to delete a movie session) - ADMIN <br/>
GET: `/orders` (to get order history for current user) - USER <br/>
GET: `/shopping-carts/by-user` (to get a shopping cart for current user) - USER <br/>
GET: `/cinema-halls ` (to get all cinema halls) - USER or ADMIN <br/>
GET: `/movies` (to get all movies) - USER or ADMIN <br/>
GET: `/movie-sessions/available` (to get all available movie by date) - USER or ADMIN <br/>
GET: `/users/by-email` (to find user by email) - ADMIN <br/>
#### Example how to add data into the application (using for example Postman)
{"email":"bob@gmail.com", "password":"bobsPass", "repeatPassword":"bobsPass"}  POST: /register <br/>
{"capacity":200, "description":"Modern hall"}  POST: /cinema-halls <br/>
{"title":"The Matrix", "description":"Will Neo be The One?"}  POST: /movies <br/>
{"movieId":1, "cinemaHallId":1, "showTime":"15.06.2022 15:15"}  POST: /movie-sessions <br/>
{"movieId":1, "cinemaHallId":1, "showTime":"15.06.2022 15:15"}  PUT: /movie-sessions/{id} <br/>

/movie-sessions/available?movieId={id}&date=dd.MM.yyyy  GET <br/>
/shopping-carts/movie-sessions?movieSessionId=1  PUT <br/>
/users/by-email?email=your email  GET <br/>

## How to start cinema service locally:
1. Install and configure Apache Tomcat (recommended version 9.0.50)
2. Install and configure and create a schema in MySQL
3. Fork and clone this project
4. To connect to database in application you need change configuration information
   in the file from `/resources/db.properties` to the ones you specified when installing MySQL
~~~
for example: 
    URL = "jdbc:mysql://localhost:3306/cinema?serverTimezone=UTC";
    USERNAME = "root";
    PASSWORD = "root";
    JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
~~~
5. Run this project using Tomcat local server

