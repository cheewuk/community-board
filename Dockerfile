# Dockerfile
# Base image: AdoptOpenJDK 17 with HotSpot JVM
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR file into the container
# The JAR file is typically located in build/libs after a Gradle build
COPY build/libs/*.jar app.jar

# Expose the port on which the Spring Boot application runs
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]



