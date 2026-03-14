# Smart Contact Manager

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-21-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-Database-orange.svg)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Template_Engine-green.svg)

**Smart Contact Manager** is a web-based application built using Spring Boot that allows users to securely manage their contacts online. It provides a platform where users can register, log in, and perform CRUD (Create, Read, Update, Delete) operations on their contacts.

## 🚀 Features

- **User Authentication & Authorization**: Secure login and registration powered by Spring Security.
- **Contact Management**: Add, view, update, and delete contacts seamlessly.
- **Smart Search**: Easily find contacts from your list.
- **Profile Management**: Users can view and update their personal profiles.
- **Email Integration**: Features email functionalities using JavaMail (e.g., OTP or notifications).
- **Responsive UI**: Built with Thymeleaf for dynamic and responsive server-side rendering.
- **Data Persistence**: Utilizes Spring Data JPA and MySQL for robust data storage.

## 🛠️ Technology Stack

- **Backend**: Java 21, Spring Boot 3.4.1
- **Security**: Spring Security
- **Database**: MySQL, Spring Data JPA
- **Frontend**: HTML, CSS, JavaScript, Thymeleaf
- **Other**: JavaMail API for email services
- **Build Tool**: Maven

## 📂 Project Structure

- `src/main/java/com/smart/Controller/`: Contains all Spring MVC Controllers handling HTTP requests.
- `src/main/java/com/smart/entities/`: JPA Entities mapping to database tables (e.g., `User`, `Contacts`).
- `src/main/java/com/smart/dao/`: Data Access Object (Repository) interfaces.
- `src/main/java/com/smart/config/`: Security and other configuration classes.
- `src/main/java/com/smart/helper/`: Utility and helper classes.
- `src/main/resources/`: Application properties, static assets, and Thymeleaf templates.

## ⚙️ How to Run Locally

### Prerequisites

- Java Development Kit (JDK) 21
- Maven
- MySQL Server

### Setup Instructions

1.  **Clone the repository**:

    ```bash
    git clone <repository-url>
    cd SmartContactManager
    ```

2.  **Configure the Database**:
    - Create a local MySQL database (e.g., `smartcontact`).
    - Update `src/main/resources/application.properties` with your database credentials:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/smartcontact
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      spring.jpa.hibernate.ddl-auto=update
      ```

3.  **Build and Run the Application**:
    You can run the application using Maven wrapper:

    ```bash
    ./mvnw spring-boot:run
    ```

    Or, if you use an IDE like Eclipse, IntelliJ, or Spring Tool Suite, you can run `SmartContactManagerApplication.java` as a Java Application.

4.  **Access the Application**:
    Open your web browser and navigate to:
    ```
    http://localhost:8080/
    ```

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check issues page.

## 📝 License

This project is open-source and available under the MIT License.
