FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM maven:3.8.5-openjdk-17-slim

WORKDIR /app

COPY target/demo*.jar demo.jar

CMD ["java", "-jar", "demo.jar"]