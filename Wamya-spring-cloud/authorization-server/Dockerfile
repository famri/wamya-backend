FROM openjdk:12.0.2

EXPOSE 9999

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
