FROM gradle:7.1.0-jdk11-hotspot as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar

FROM openjdk:11-jre-slim
COPY --from=builder /home/gradle/src/app/build/libs/app-all.jar /app/
WORKDIR /app
CMD ["java", "-jar", "app-all.jar"]