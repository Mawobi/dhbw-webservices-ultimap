FROM openjdk:15

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080/tcp

ENTRYPOINT ["java", "-jar", "/app.jar"]
HEALTHCHECK --start-period=10s CMD curl -f http://localhost:8080/actuator/health || exit 1