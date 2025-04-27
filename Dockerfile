# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR file
COPY --from=builder /app/target/*.jar app.jar

# Set environment variables
ENV PORT=8080

# Expose the port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"] 