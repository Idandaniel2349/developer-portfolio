# 1. We use Maven + Temurin JDK to compile the code and then create an app folder
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

# 2. Copy POM files to save time when dependencies don't change
COPY pom.xml .
COPY dungeon-explorer-api/pom.xml dungeon-explorer-api/
COPY dungeon-explorer-dtos/pom.xml dungeon-explorer-dtos/
COPY dungeon-explorer-integration-tests/pom.xml dungeon-explorer-integration-tests/
COPY dungeon-explorer-model/pom.xml dungeon-explorer-model/
COPY dungeon-explorer-service/pom.xml dungeon-explorer-service/
COPY dungeon-explorer-tests/pom.xml dungeon-explorer-tests/

# 3. Download dependencies into the container's internal cache
RUN mvn dependency:go-offline -B

# 4. Copy source and build without tests
COPY dungeon-explorer-api/src dungeon-explorer-api/src
COPY dungeon-explorer-dtos/src dungeon-explorer-dtos/src
COPY dungeon-explorer-integration-tests/src dungeon-explorer-integration-tests/src
COPY dungeon-explorer-model/src dungeon-explorer-model/src
COPY dungeon-explorer-service/src dungeon-explorer-service/src
COPY dungeon-explorer-tests/src dungeon-explorer-tests/src
RUN mvn clean package -DskipTests

# 5. Use a lightweight JRE (Runtime only) for the final image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 6. Copy the JAR from the module that contains the @SpringBootApplication
COPY --from=build /app/dungeon-explorer-api/target/*-exec.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]