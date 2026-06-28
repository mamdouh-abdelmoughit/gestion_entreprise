# ── Stage 1: build the JAR ────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Cache the dependency layer separately from source code
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

COPY src ./src
RUN mvn clean package -DskipTests -B -q

# ── Stage 2: minimal runtime image ────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy only the final JAR
COPY --from=build /app/target/spring-boot-btp-backend-1.0.0.jar app.jar

# Render sets PORT via env var; default 8080
EXPOSE 8080

# -Xmx400m keeps us within Render's 512 MB free-tier RAM limit
ENTRYPOINT ["java", "-Xmx400m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
