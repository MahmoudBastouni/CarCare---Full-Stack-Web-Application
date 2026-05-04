# CarCare — Full Stack Web Application (Academic Project)

CarCare is a simulated mobile car wash and detailing booking platform 
built as a university group project. This is not a real business — 
the project was built purely to demonstrate full-stack web development 
skills including frontend design, backend logic, and database management.

## Features
- Browse service packages loaded dynamically from a MySQL database
- Submit and manage bookings online
- Look up bookings by phone number and cancel them
- Admin dashboard with login authentication
- Admin can update booking statuses and service prices in real time

## Technologies Used
- Frontend: HTML, CSS, Bootstrap, JavaScript
- Backend: Java Servlets (Jakarta EE)
- Database: MySQL
- Server: Apache Tomcat
- IDE: Eclipse

## Database
The application uses a MySQL database called carcare_db with three tables:
- services — stores the service packages and pricing
- bookings — stores all customer booking submissions
- admins — stores admin login credentials

## How to Run
1. Install Eclipse IDE for Enterprise Java Developers, Apache Tomcat 10, and MySQL
2. Import the project into Eclipse as an existing project
3. Run the SQL script to create the database and tables
4. Update DBConnection.java with your MySQL username and password
5. Add the MySQL connector JAR to WEB-INF/lib
6. Run the project on the Tomcat server
7. Open your browser and go to:
   http://localhost:8080/CarCare/html/index.html

## Admin Access
- URL: http://localhost:8080/CarCare/html/login.html
- Username: admin
- Password: admin123

## Note
This project was built for academic purposes to demonstrate web development
skills. It is not deployed and does not represent a real business or service.
