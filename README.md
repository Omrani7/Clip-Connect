**Clip-Connect PFA Backend**

This is the backend service for the Clip-Connect project, a barber shop exploration platform enabling users to find barber shops, explore maps, and view barber statistics. Built with Spring Boot and integrated with a MySQL database via XAMPP, this backend supports the frontend available [here](https://github.com/ghaithghawarrr/clip-connect-pfa-frontend.git).

### Features

- **User Management**: Secure user authentication and authorization using Spring Security.
- **Barber Shop Data**: Manages information on barber shop locations, services, reviews, and ratings.
- **Email Support**: Uses Spring Boot's email capabilities for user notifications.
- **Map Integration**: Provides APIs for displaying barber shop locations on maps.
- **Analytics and Statistics**: Aggregates and provides data on barbers and user interactions.

### Dependencies

- **Spring Boot Starters**:
  - **Data JPA**: For database interaction with MySQL.
  - **Mail**: For sending user notifications.
  - **Security**: Manages authentication and authorization.
  - **Web**: Builds RESTful APIs.
  - **Thymeleaf**: For server-side rendering if needed.
  - **WebFlux**: Supports reactive programming.
- **MySQL Connector**: Connects to MySQL via XAMPP.
- **Jackson**: For JSON processing.
- **Lombok**: Reduces boilerplate code with annotations.
- **DevTools**: Enables hot reloading during development.

### Getting Started

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Omrani7/Clip-Connect.git
   cd Clip-Connect
   ```

2. **Configure Database (XAMPP)**:
   Start XAMPP, set up a MySQL database, and configure `application.properties` with your database credentials.

3. **Build and Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   The server will start on [http://localhost:8080](http://localhost:8080) by default.

4. **API Documentation**:
   Access API documentation via Swagger (if configured) at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
