# Use an OpenJDK image with JDK 23 or newer
FROM eclipse-temurin:23-jdk

# Install netcat (for wait-for-db.sh)
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Create a directory for the app
WORKDIR /app

# Copy the JAR and wait script into the container
COPY build/libs/ebooklibrarybackend-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-db.sh wait-for-db.sh

# Make wait script executable
RUN chmod +x wait-for-db.sh

# Expose port 8080 for the backend service
EXPOSE 8080

# Use the wait script as entrypoint
ENTRYPOINT ["./wait-for-db.sh"]
