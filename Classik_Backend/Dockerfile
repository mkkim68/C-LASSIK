# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/Classik_Backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
