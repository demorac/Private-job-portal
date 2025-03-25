# Use official Maven image with JDK 17 for building
FROM maven:3.8.5-eclipse-temurin-17 AS build

# Set working directory
# WORKDIR /app

# Copy project files
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JDK 17 image for runtime
FROM openjdk:17.0.1-jdk-slim

# Set working directory
# WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /target/Job-Portal-0.0.1-SNAPSHOT.jar Job-Portal.jar

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "Job-Portal.jar"]
