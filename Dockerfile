# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk
# Set the working directory inside the container
WORKDIR /app

# Copy the Maven or Gradle build artifact (JAR) to the container
COPY target/*.jar app.jar

# Expose the port the app will run on (Spring Boot defaults to port 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
