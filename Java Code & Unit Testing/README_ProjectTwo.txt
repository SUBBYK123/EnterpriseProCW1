
Web Interface for Dataset Management and Visualization
======================================================

Overview
--------
This project is a full-stack web application built as part of Project Two for the Enterprise Pro module at the University of Bradford.
The application enables users to upload, manage, and visualize geographical datasets on an interactive Google Map. 
Admins can control user access, approve dataset requests, and manage uploaded datasets and individual assets.

Key Features
------------
- User authentication and registration with Spring Security
- Role-based access control (ADMIN and USER)
- Upload and preview datasets (CSV/JSON) on Google Maps
- Display and categorize map markers by dataset
- Add, edit, and delete manual assets with UI and database integration
- Request and approve access to datasets
- Log important user actions (login, logout, registration, password reset)
- Email notifications for events like access requests and registration
- Download datasets upon access approval
- Admin dashboard for managing users, datasets, and requests
- Logs dashboard with filtering functionality
- Font size accessibility controls
- Fully responsive UI using Tailwind CSS
- Weekly team meeting documentation included

Technologies Used
-----------------
Backend:
- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- JDBC (manual implementation, no JPA)
- MySQL
- Thymeleaf
- Gmail API and Jakarta Mail

Frontend:
- HTML5, CSS3
- Tailwind CSS
- JavaScript
  - Google Maps API
  - JustValidate
  - Proj4js

Project Structure
-----------------
Backend (Spring Boot):
- controller/
- model/
- repository/
- service/
- security/
- WebinterfaceApplication.java

Frontend:
- templates/ (Thymeleaf HTML templates)
- static/css/styles.css
- static/js/map.js, validate.js

Database Tables
---------------
- users
- dataset_metadata
- individual_assets
- dataset_access_requests
- logs
- and more added in the project_two sql document in the java code.
Functional Highlights
---------------------
User-Side:
- Register and log in
- View public datasets
- Request and download datasets
- Add/edit/delete personal assets
- Responsive UI with accessibility support

Admin-Side:
- Approve/deny dataset requests
- Manage datasets
- View logs
- Assign departments and roles to users

Email Notifications
-------------------
- OTP and registration confirmation
- Notify admin on new access request
- Notify user upon approval

Weekly Meeting Minutes
----------------------
Documented from February 3rd onwards.

Setup Instructions
------------------
1. Clone the repository
2. Set up MySQL and create required tables
3. Used MySQL Workbench for this and Sql Server 8.4, so it is recommended to use that.
3. Configure application.properties:
   - spring.datasource.url
   - spring.datasource.username
   - spring.datasource.password
4. Add Gmail credentials (optional)
5. Run the application:
   ./mvnw spring-boot:run
6. Access at http://localhost:8080
7. Admin Details(Remember to change): email:    mustafakamran46@hotmail.com
                                      password: password



