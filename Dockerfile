# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (improves Docker caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Grant execute permissions to Maven wrapper
RUN chmod +x ./mvnw

# Copy the entire project (source code)
COPY src/ src/

# Run Maven build inside the container to generate the JAR
RUN ./mvnw clean package -DskipTests

# Copy the generated JAR to run the application
CMD ["java", "-jar", "target/Job-Portal-0.0.4-SNAPSHOT.jar"]
