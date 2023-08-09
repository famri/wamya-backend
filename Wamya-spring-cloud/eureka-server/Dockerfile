FROM openjdk:12.0.2

EXPOSE 8761

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Xms256m","-Xms350m","-jar","/app.jar"]
