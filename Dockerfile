# Use a lightweight OpenJDK image
FROM openjdk:17-jdk-slim

# Copy the fat JAR into the container
COPY target/*.jar app.jar

# Specify the default command to run the fat JAR
CMD ["java", "-jar", "app.jar"]

