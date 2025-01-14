ARG ICS_PATH=/usr/src/ipcloudsync

FROM gradle:8.1.1-jdk17 AS build

ARG ICS_PATH
WORKDIR $ICS_PATH

COPY settings.gradle.kts gradlew gradlew.bat $ICS_PATH/
COPY app/build.gradle.kts $ICS_PATH/app/
COPY gradle $ICS_PATH/gradle

RUN ./gradlew dependencies --no-daemon

COPY app $ICS_PATH/app

RUN ./gradlew shadowJar --no-daemon

FROM openjdk:17-jdk-slim AS runtime

ARG ICS_PATH

WORKDIR /app

COPY --from=build $ICS_PATH/app/build/libs/app-all.jar /app/app-all.jar

CMD ["java", "-jar", "/app/app-all.jar"]