# Build Stage: Use Maven with OpenJDK for compiling the application
FROM maven:3.8-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies (to cache dependencies)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code and build the application
COPY . .
RUN mvn clean package -DskipTests

# Run Stage: Use a smaller OpenJDK runtime image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (Spring Boot default port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
