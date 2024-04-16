# Use the official Maven image to build the project
FROM maven:3.8.4-openjdk-11-slim AS build

# Copy the project files to the container
COPY src /home/app/src
COPY pom.xml /home/app

# Set the working directory
WORKDIR /home/app

# Build the application
RUN mvn clean package

# Use OpenJDK 11 slim image for a smaller image size
FROM openjdk:11-jre-slim

# Copy the built JAR file from the build stage
COPY --from=build /home/app/target/*.jar /usr/local/lib/myapi.jar

# Expose port 8080 for the application
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/usr/local/lib/myapi.jar"]
