# --- ETAPA 1: Compilación ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiar el archivo de configuración de Maven y el código fuente
COPY pom.xml .
COPY src ./src

# Compilar el proyecto saltando las pruebas unitarias para acelerar el despliegue inicial
RUN mvn clean package -DskipTests

# --- ETAPA 2: Ejecución ---
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app

# Copiar el archivo .jar compilado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]