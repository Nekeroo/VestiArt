# Stage 1: Build de l'application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Installation de Maven
RUN apk add --no-cache maven

# Définition du répertoire de travail pour le build
WORKDIR /build

# Copie du fichier de configuration Maven
COPY pom.xml .

# Téléchargement des dépendances (pour optimiser le cache Docker)
RUN mvn dependency:resolve

# Copie du code source
COPY src ./src

# Compilation de l'application
RUN mvn clean package -DskipTests

# Stage 2: Image de production
FROM eclipse-temurin:21-jre-alpine

# Définition du répertoire de travail
WORKDIR /app

# Copie du JAR depuis le stage de build
COPY --from=builder /build/target/*.jar app.jar

# Exposition du port (port par défaut de Spring Boot)
EXPOSE 8080

# Configuration des variables d'environnement JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Commande de santé pour Docker (optionnel)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Commande pour lancer l'application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]