FROM gradle:7.1.0-jdk11-hotspot as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar

FROM openjdk:11-jre-slim
WORKDIR /home/zenebrisserver
RUN useradd -ms /bin/bash zenebrisserver -d /home/zenebrisserver
COPY --from=builder /home/gradle/src/app/build/libs/app-all.jar /home/zenebrisserver
RUN chown -R zenebrisserver /home/zenebrisserver
EXPOSE 8080/tcp
USER zenebrisserver
CMD ["java", "-jar", "app-all.jar"]