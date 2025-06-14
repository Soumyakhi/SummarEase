#!/bin/bash

# Start MySQL service
service mysql start

# Wait a bit for MySQL to initialize
echo "Waiting for MySQL to initialize..."
sleep 10

# Start your Spring Boot application
java -jar /app/pdfAi.jar
