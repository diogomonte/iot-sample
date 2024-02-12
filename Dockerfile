FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=build /app/target/iot.jar iot.jar
EXPOSE 8080
CMD ["java", "-jar", "iot.jar"]