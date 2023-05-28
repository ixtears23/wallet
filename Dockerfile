FROM openjdk:20-slim

RUN apt-get update && apt-get install -y findutils

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew --version

COPY . .
RUN ./gradlew clean build

EXPOSE 8081

CMD ["java", "-jar", "/app/build/libs/crypto-wallet-0.0.1-RELEASE.jar"]