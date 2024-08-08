FROM openjdk:17-jdk-alpine
COPY /build/libs/Smart-File-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]