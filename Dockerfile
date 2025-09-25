# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Copy data directory if it exists (for file-based storage)
#COPY data ./data

# Build the application with Spring Boot plugin
RUN mvn clean package spring-boot:repackage -DskipTests -Dspring-boot.run.profiles=prod

# Create a directory for the application
RUN mkdir -p /app/runtime

# Copy the built JAR
RUN cp target/reconnect-1.0-SNAPSHOT.jar /app/runtime/app.jar

# Copy resources and data to runtime directory
RUN cp -r src/main/resources/* /app/runtime/ 2>/dev/null || true
RUN cp -r data /app/runtime/ 2>/dev/null || true

# Set working directory to runtime
WORKDIR /app/runtime

# Expose the port (Spring Boot default is 8080)
EXPOSE ${PORT:-8080}

# Set environment variables for production
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=${PORT:-8080}

# Run the application
CMD ["java", "-jar", "app.jar"]
