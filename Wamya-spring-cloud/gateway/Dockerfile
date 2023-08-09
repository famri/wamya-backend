FROM openjdk:12.0.2

EXPOSE 8443

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Xms128m","-Xmx450m","-jar","/app.jar"]
