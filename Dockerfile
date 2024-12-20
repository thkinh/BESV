FROM openjdk:17-jdk-alpine
COPY build/libs/spring_api-0.0.1-SNAPSHOT.jar spring_api-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spring_api-0.0.1-SNAPSHOT.jar"]