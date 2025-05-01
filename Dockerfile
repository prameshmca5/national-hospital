# Use full JDK base image (more compatible)
FROM --platform=linux/amd64 eclipse-temurin:17-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
