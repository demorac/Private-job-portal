# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml first to leverage Docker caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Grant execute permissions to Maven wrapper
RUN chmod +x ./mvnw

# Run Maven build to create the JAR file
RUN ./mvnw clean package -DskipTests

# Copy only the built JAR file to the container
COPY target/Job-Portal-0.0.4-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
