# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the source code
COPY . .

# Build the application using Maven (inside Docker)
RUN ./mvnw clean package -DskipTests

# Copy the generated JAR file to the container
COPY target/Job-Portal-0.0.4-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
