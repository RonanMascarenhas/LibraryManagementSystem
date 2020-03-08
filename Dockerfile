FROM openjdk:8-jre-alpine

COPY target/main-0.0.1-SNAPSHOT.jar /lms.jar
CMD java -jar lms.jar