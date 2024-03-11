FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/weather-service-1.0.0.jar ./weather-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "weather-service.jar"]
