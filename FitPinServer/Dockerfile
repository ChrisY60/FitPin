FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x gradlew && \
    ( for i in 1 2 3 4 5; do \
        ./gradlew bootJar --no-daemon -x test && exit 0; \
        echo "Gradle build failed (attempt $i/5), retrying in 15s..."; \
        sleep 15; \
      done; \
      exit 1 )
RUN mv build/libs/FitPinServer-0.0.1-SNAPSHOT.jar build/libs/app.jar


FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]