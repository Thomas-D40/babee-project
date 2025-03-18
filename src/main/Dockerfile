# Étape 1 : Build Angular
FROM node:22 AS frontend-build
WORKDIR /app/frontend
COPY ./frontend/ .
RUN npm install
RUN npm run build --prod

# Étape 2 : Build Spring Boot
FROM eclipse-temurin:21-jdk AS backend-build
WORKDIR /app
COPY ./src/main/ .
RUN ./mvnw clean package -DskipTests

# Étape 3 : Image finale
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
