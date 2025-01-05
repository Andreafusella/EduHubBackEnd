# EduHub-Backend

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Javalin](https://img.shields.io/badge/Javalin-5A67D8?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)

The backend of the **EduHub** project serves as the core system for managing lessons, students, and associated data. It is developed with **Java using the Javalin framework**, leveraging **Maven** for dependency management and **PostgreSQL** as the database to ensure robust and efficient data storage. The backend provides RESTful APIs for seamless communication with the frontend and ensures secure, scalable, and efficient operations.

## System Requirements
- **Java Development Kit (JDK)**: Version **11** or higher.
- **Apache Maven**: Version **3.6.0** or higher.
- **PostgreSQL**: Port **8001**

## Installation

1. Clone the repository:
```bash
git clone https://github.com/Andreafusella/EduHubBackEnd.git
cd EduHubBackEnd
```
2. Configure the database:
- Create a new database named **EduHub**
- Import the file **src/Database/database.sql** in the database or use the DDL in the file **src/Database/database.sql** to create the structure of the database.

3. Configure the connection to the database:
- Open the file **src/main/java/com/andrea/utility/database/DatabaseConnection.java**
- Change the user and password of the database in the file **src/main/java/com/andrea/utility/database/DatabaseConnection.java**.

4. Create Admin Account:
- Open Postman and create a new POST request to the endpoint **http://localhost:8000/register** with the following body:
```json
{
    "name": "Admin1",
    "last_name": "Admin1",
    "role": "Administrator",
    "email": "admin@gmail.com",
    "password": "Password123",
    "avatar": 2
}
```

# Run the project

1. Run the project:

## Access at Backend
- The application will be available at URL:
```bash
http://localhost:8000
```

# Principal Endpoints

## Account
- **POST** /register: Register a new account.
- **POST** /login: Login to the system.

## Course
- **GET** /courses: Get all courses.
- **POST** /add-course: Add a new course.

## Enrolled
- **POST** /enrolled: Enroll a student in a course.
- **DELETE** /enrolled: Delete a student from a course.

## Lesson
- **DELETE** /lesson?id_lesson=3: Delete a lesson.


# Project Structure

- **controller**: Contains the controllers of the application.
- **dao**: Contains the data access objects of the application.
- **dto**: Contains the data transfer objects of the application.
- **exception**: Contains the exceptions of the application.
- **model**: Contains the models of the application.
- **service**: Contains the services of the application.
- **utility**: Contains the utilities of the application.
- **validator**: Contains the validators of the application.

```plaintext
.
├── src/
│   ├── Database/
│   │   └── database.sql
│   └── Main/
│       └── java/com/andrea/
│           ├── auth/
│           │   ├── controller/
│           │   │   └── AuthController.java
│           │   ├── dao/
│           │   │   └── AuthDao.java
│           │   ├── dto/
│           │   │   └── LoginDto.java
│           │   ├── middleware/
│           │   │   └── JwtAuthMiddleware.java
│           │   └── util/
│           │       └── JwtUtil.java
│           ├── controller/
│           │   └── ...java
│           ├── dao/
│           │   └── ...java
│           ├── dto/
│           │   └── ...java
│           ├── exception/
│           │   └── ...java
│           ├── model/
│           │   └── ...java
│           ├── service/
│           │   └── ...java
│           ├── utility/
│           │   ├── database/
│           │   │   └── DatabaseConnection.java
│           │   ├── email/
│           │   │   └── EmailService.java
│           │   └── validator/
│           │       └── ...java
│           └── Main.java
├── pom.xml
└── README.md
```

### Contact
- For any questions or suggestions, please contact me at:
  - **Email**: andrea55fusella@gmail.com
  - **LinkedIn**: [Andrea Fusella](www.linkedin.com/in/andrea-fusella)
