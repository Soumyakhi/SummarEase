# Use a lightweight JDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app
RUN apt-get update && apt-get install -y \
    fonts-noto \
    && rm -rf /var/lib/apt/lists/*
# Copy your JAR into the container
COPY target/pdfAi-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (default Spring Boot port)
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
