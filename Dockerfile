FROM openjdk:latest
VOLUME /tmp
COPY src/main/res/database-data ./database-data
COPY src/main/res/database-schema ./database-schema
ADD target/NoSQL-Database-Atypon-Final-0.0.1-SNAPSHOT.jar NoSQL-Database-Atypon-Final-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","NoSQL-Database-Atypon-Final-0.0.1-SNAPSHOT.jar"]
