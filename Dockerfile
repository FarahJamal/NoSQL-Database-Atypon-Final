FROM openjdk:latest
COPY src/main/res/database-data ./database-data
COPY src/main/res/database-schema ./database-schema
COPY target/FinalProject-1.0-SNAPSHOT.jar FinalProject-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","FinalProject-1.0-SNAPSHOT.jar"]
EXPOSE 8082
