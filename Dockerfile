# Étape 1 : Build avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/application-prod.properties /app/application-prod.properties

EXPOSE 8080
# Lancement avec le profil prod
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/application-prod.properties,/app/application-prod.properties", "--spring.profiles.active=prod"]