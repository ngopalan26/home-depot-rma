# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better caching
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

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

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT}/api/travel/health || exit 1

# Run the application
CMD ["java", "-jar", "-Dserver.port=${PORT}", "-Dspring.profiles.active=prod", "target/travel-packing-assistant-0.0.1-SNAPSHOT.jar"]
