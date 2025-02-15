# Users Service 🚀

## Overview
The **Users Service** is a microservice built with **Java Spring Boot**, following **Domain-Driven Design (DDD)** principles based on the **Codely guide**. It provides user and tenant management for a multi-tenant SaaS platform.

## 📌 Key Features
- **Domain-Driven Design (DDD)** architecture
- **CQRS pattern** with Command & Query separation
- **GraphQL API** for user and tenant management
- **Event-driven architecture** using RabbitMQ
- **MariaDB** with **Hibernate** for persistence
- **Hexagonal Architecture** for better modularity

## 📂 Project Structure

```
com
└── devsoftec
    └── jaap
        └── users
            ├── application
            │   ├── create
            │   │   ├── CreateUserCommandHandler.java
            │   │   ├── CreateUserCommand.java
            │   │   └── UserCreator.java
            │   ├── find
            │   │   ├── FindUserQueryHandler.java
            │   │   ├── FindUserQuery.java
            │   │   └── UserFinder.java
            │   ├── search
            │   │   ├── all
            │   │   │   ├── AllUsersSearcher.java
            │   │   │   ├── SearchAllUsersQueryHandler.java
            │   │   │   └── SearchAllUsersQuery.java
            │   │   └── bycriteria
            │   │       ├── SearchUsersByCriteriaQuery.java
            │   │       ├── UsersByCriteriaSearcherHandler.java
            │   │       └── UsersByCriteriaSearcher.java
            ├── domain
            │   ├── events
            │   │   └── UserCreatedDomainEvent.java
            │   ├── User.java
            │   ├── UserNotExist.java
            │   ├── UserRepository.java
            │   └── ValueObjects
            │       ├── UserId.java
            │       ├── UserEmail.java
            │       ├── UserName.java
            │       ├── UserLastName.java
            │       ├── UserCreatedAt.java
            │       ├── UserUpdatedAt.java
            ├── infrastructure
            │   ├── controller
            │   │   ├── graphql
            │   │   │   ├── UserGetControllerGraphql.java
            │   │   │   └── UserPostControllerGraphql.java
            │   │   ├── rest
            │   │   │   ├── UserGetController.java
            │   │   │   ├── UsersPutController.java
            │   │   │   └── SearchCriteria.java
            │   ├── persistence
            │   │   ├── hibernate
            │   │   │   └── User.orm.xml
            │   │   ├── InMemoryUserRepository.java
            │   │   └── MariaDBUserRepository.java
            ├── Starter.java
```

## 🛠 Technologies Used
- **Java 21+**
- **Spring Boot**
- **GraphQL**
- **MariaDB** + **Hibernate**
- **RabbitMQ** (for event-driven communication)
- **Docker** (for containerized deployment)

## ⚙️ Configuration

### 1️⃣ Prerequisites
Ensure you have the following installed:
- **Java 21+**
- **Gradle**
- **Docker & Docker Compose** (for MariaDB & RabbitMQ setup)

### 2️⃣ Environment Variables
Create a `.env` file with the following:

```
# DATABASE MARIADB
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_NAME=skeleton
DATABASE_USER=root
DATABASE_PASSWORD=


# RabbitMQ
RABBITMQ_HOST=
RABBITMQ_PORT=5672
RABBITMQ_LOGIN=
RABBITMQ_PASSWORD=
RABBITMQ_EXCHANGE="domain_events"
RABBITMQ_MAX_RETRIES=5
RABBITMQ_VHOST=

```

### 3️⃣ Running the Application

#### Start the database and RabbitMQ
```sh
docker-compose up -d
```

#### Build & Run
```sh
git clone https://github.com/your-repo/users-service.git
cd users-service
./gradlew clean build
./gradlew bootRun --args='--spring.profiles.active=local'
```

## 🔗 API Endpoints

### GraphQL API
- `createUser(username: String, email: String): User` → Create a new user
- `getUser(id: String): User` → Get user details
- `searchUsers(criteria: SearchCriteria): [User]` → Search users with filters

## 🛠 Contributing
1. Fork the repo
2. Create a new branch (`git checkout -b feature-xyz`)
3. Commit your changes (`git commit -m 'Added feature xyz'`)
4. Push to the branch (`git push origin feature-xyz`)
5. Create a Pull Request

---
🚀 **Built with ❤️ using Java & Spring Boot** 🚀 by [DEVSOFTEC](https://www.devsoftec.com/jaap) team

