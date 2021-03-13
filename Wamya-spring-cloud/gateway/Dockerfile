FROM openjdk:12.0.2

EXPOSE 8443

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Xms256m","-Xmx350m","-jar","/app.jar"]
