# Étape 1: Utiliser l'image de base avec OpenJDK
FROM openjdk:17-jdk-slim as builder

# Étape 2: Définir le répertoire de travail
WORKDIR /app

# Étape 3: Copier le fichier JAR du projet dans le container
# Copier le fichier JAR de votre build local
COPY target/*.jar app.jar

# Étape 4: Exposer le port de l'application
EXPOSE 9000

# Étape 5: Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
