FROM openjdk:8-jdk-alpine
ENV SPRING_PROFILES_ACTIVE=production
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} withkid-auth-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/withkid-auth-server.jar"]


