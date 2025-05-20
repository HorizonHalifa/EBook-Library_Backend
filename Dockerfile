# Use an OpenJDK image with JDK 23 or newer
FROM eclipse-temurin:23-jdk

# Create a directory for the app
WORKDIR /app

# Copy the built JAR into the container
COPY build/libs/ebooklibrarybackend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the backend service
EXPOSE 8080

# Set the entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
