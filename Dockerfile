FROM openjdk:20-jdk

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew --version

COPY . .
RUN ./gradlew build

EXPOSE 8081

CMD ["java", "-jar", "/app/build/libs/app-0.0.1-SNAPSHOT.jar"]