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

## SQL Setup Script
Run this in MySQL Workbench to create the database and tables:
Please note that you should run each section of the script one by one.

```sql
CREATE DATABASE carcare_db;
USE carcare_db;

CREATE TABLE services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    label VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    duration_text VARCHAR(50),
    features TEXT,
    icon VARCHAR(10)
);

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(100),
    address TEXT NOT NULL,
    service_id INT NOT NULL,
    car_type VARCHAR(50) NOT NULL,
    car_model VARCHAR(100),
    car_color VARCHAR(50),
    booking_date DATE NOT NULL,
    booking_time VARCHAR(20) NOT NULL,
    notes TEXT,
    status VARCHAR(30) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

INSERT INTO services (title, description, label, price, duration_text, features, icon) VALUES
('Basic Wash', 'A simple option for keeping the exterior and interior looking fresh.', 'Most Popular', 50.00, '45 minutes', 'Exterior hand wash,Wheel & tire clean,Window wipe-down,Interior vacuum', '🪣'),
('Premium Detail', 'A deeper clean for customers who want better inside and outside results.', 'Best Value', 200.00, '90 minutes', 'Everything in Basic Wash,Full interior detailing,Dashboard and seat clean,Exterior wax finish', '✨'),
('Ceramic Coating', 'A stronger protection package that helps the paint stay glossy for longer.', 'Premium Protection', 750.00, '3 to 4 hours', 'Everything in Premium Detail,Professional ceramic layer,Hydrophobic gloss finish,UV and scratch resistance', '🛡️');

INSERT INTO admins (username, password_hash) VALUES ('admin', 'admin123');
```
## How to Run
1. Install Eclipse IDE for Enterprise Java Developers, Apache Tomcat 10, and MySQL
2. Import the project into Eclipse as an existing project
3. Run the SQL script above to create the database and tables
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
