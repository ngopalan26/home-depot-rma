# Multi-stage build: Build stage
FROM maven:3.8.6-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/travel-packing-assistant-0.0.1-SNAPSHOT.jar app.jar

# Create a non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Change ownership of the app directory
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose port 8080 (Cloud Run uses PORT environment variable)
EXPOSE 8080

# Set environment variable for Cloud Run
ENV PORT=8080

# Run the application
CMD ["java", "-jar", "-Dserver.port=${PORT}", "-Dspring.profiles.active=prod", "app.jar"]