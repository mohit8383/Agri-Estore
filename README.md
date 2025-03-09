# Agri-Estore

## Overview
Agri-Estore is an online platform built using Spring Boot that provides farmers and agricultural businesses with a marketplace to buy and sell farming products, tools, and resources. The platform integrates various features such as product listings, user authentication, order management, and secure payment options.

## Features
- **User Authentication**: Secure login and registration system.
- **Product Management**: Add, update, and remove agricultural products.
- **Order Processing**: View and manage customer orders efficiently.
- **Payment Integration**: Secure payment gateways for transactions.
- **Admin Dashboard**: Manage users, products, and orders.
- **Search & Filters**: Find products easily with advanced search and filter options.

## Technologies Used
- **Backend**: Spring Boot, Spring Security, Hibernate, JPA
- **Database**: MySQL
- **Frontend**: React.js (if applicable) or Thymeleaf
- **Cloud Services**: AWS S3 for image storage
- **Other Tools**: Maven, Docker (optional), Swagger for API documentation

## Installation & Setup
### Prerequisites
- Java 17+
- Maven
- MySQL Database
- Git

### Steps to Run
1. **Clone the repository**
   ```bash
   git clone https://github.com/mohit8383/Agri-Estore.git
   cd Agri-Estore
   ```
2. **Configure Database**
   - Update `src/main/resources/application.properties` with your database credentials.

3. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the Application**
   - The API will be available at: `http://localhost:8080`
   - Swagger UI (if enabled): `http://localhost:8080/swagger-ui.html`

## Environment Variables
To keep credentials secure, use environment variables instead of hardcoding them in `application.properties`:
```properties
AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
AWS_SECRET_KEY=${AWS_SECRET_KEY}
DB_USERNAME=${DB_USERNAME}
DB_PASSWORD=${DB_PASSWORD}
```

## Contributing
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-branch`
3. Commit changes: `git commit -m "Added new feature"`
4. Push to the branch: `git push origin feature-branch`
5. Open a Pull Request.

## License
This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

## Contact
For any queries, contact:
```plaintext
Developer: Mohit Kasat
Email: mohitkasat83@gmail.com
GitHub: https://github.com/mohit8383

Developer: Balwinder Singh
Email: 1231262balwindersingh@gmail.com
```

