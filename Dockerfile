FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean build -x test -x spotlessJavaCheck

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/users-service-0.0.1.jar /app/users-service.jar

EXPOSE 8080

CMD ["java", "-jar", "users-service.jar"]
