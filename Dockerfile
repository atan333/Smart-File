FROM openjdk:24-slim-bookworm
COPY /build/libs/Smart-File-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]