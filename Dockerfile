FROM java:openjdk-8-jdk-alpine
WORKDIR /tmp
COPY /target/eventmanager-*.jar .
CMD ["java", "-jar", "eventmanager-0.0.1-SNAPSHOT.jar"]
