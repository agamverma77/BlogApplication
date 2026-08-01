# ----------------------------------------------------
# Stage 1: Build the Spring Boot Application
# ----------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /workspace

# Copy Maven descriptor
COPY pom.xml .

# Pre-download dependencies for caching
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src src

# Package the executable JAR (skipping tests for fast build)
RUN mvn clean package -DskipTests -B

# ----------------------------------------------------
# Stage 2: Minimal Production Runtime
# ----------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add a non-root user for enhanced container security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy packaged JAR from builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Cloud Port Exposure
EXPOSE 8080

# Configure JVM flags and launch the app
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
