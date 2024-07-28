# CircoloCulturale
"CircoloCulturale" is a University project developed for "University of Ferrara" during the course of "Web Technologies" in the academic year 2023/2024. The project is a web application that allows users to create and manage events, courses, bookings and general administration of a cultural club.
## Technologies
We used a monolithic architecture to develop the project. The core of the application is SpringBoot configured to deploy Jakarta Servlets. The ViewModel is structured upon JSP files alongside CSS and JavaScript.
The database is managed by PostgreSQL and the ORM is handled by Hibernate.
## Installation
To run the project you need to have a PostgreSQL database running on your machine. The database parameters, such as URL endpoint and authentication, must be correctly provided in the `application.properties` file.
As it would've been a hassle to also include the whole structure of our database, you can simply replace `spring.jpa.hibernate.ddl-auto=validate` to `spring.jpa.hibernate.ddl-auto=create` to let the application automatically populate the database with the correct entities described in Hibernate.

!! **WARNING** !! We have never tested the application with an empty database, so we cannot guarantee that it will work as expected.

Email functionality relies on a Gmail account. You need to provide your Gmail credentials wherever a "send email" method is configured to let the application send emails.

!! **WARNING** !! Gmail now requires a two-step verification process to allow third-party applications to send emails. You need to create an "App Password" in your Google Account settings and use it instead of your standard Gmail login password.

## Usage
Simply run the application with `mvn spring-boot:run` and navigate to `localhost:8080` to access the application. Make sure to allow cookies to be stored in your browser, as the application relies on them to keep track of the user session.

## Known issues
- The application is not fully responsive and may not display correctly on mobile devices.
- The application is not fully tested and may contain bugs.
- The application is not fully secure and may contain vulnerabilities.
- The application is not fully optimized and may contain performance issues.
- The application is not fully documented and may contain unclear parts.
- The application has not been structured properly and might be hard to maintain.

We didn't have a lot of time to complete this project, therefore forgive us for such shortcomings!