# Use a base image with JDK and Maven installed to build the Spring Boot application
FROM adoptopenjdk/openjdk11:jre-11.0.12_7-jdk-hotspot-focal AS builder

# Set the working directory in the container
WORKDIR /app

# Clone the repository
RUN apt-get update && apt-get install -y git \
    && git clone https://github.com/engsaw/myapi.git .

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Use a lightweight base image with JRE to run the Spring Boot application
FROM adoptopenjdk/openjdk11:jre-11.0.12_7-jdk-hotspot-focal

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the builder stage to the current location in the container
COPY --from=builder /app/target/myapi.jar .

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "myapi.jar"]
