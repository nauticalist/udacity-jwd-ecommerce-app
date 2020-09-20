FROM adoptopenjdk/openjdk11:latest

EXPOSE 8080

ADD ./target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar","/app.jar"]
