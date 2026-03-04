FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR app/
COPY gradlew build.gradle settings.gradle ./
COPY ./gradle ./gradle
RUN ./gradlew dependencies --no-daemon
COPY ./src ./src
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]