FROM openjdk:latest

ARG NODE_ENV

VOLUME /tmp

RUN apk --no-cache add bash ngix
RUN mkdir -p /run/ngix/ && chown ngix:ngix /run/ngix && touch /run/ngix/ngix.pid
COPY etc/ngix.conf /etc/ngix/http.d/default.conf

RUN mkdir /app
COPY . /app
WORKDIR /app

RUN mv bin/* /bin

RUN if [$NODE_ENV !="development"]; then \
    mvn spring-boot:run \
    mv build /build \
    fi

COPY src/main/res/database-data ./database-data
COPY src/main/res/database-schema ./database-schema
ADD target/NoSQL-Database-Atypon-Final-0.0.1-SNAPSHOT.jar NoSQL-Database-Atypon-Final-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","NoSQL-Database-Atypon-Final-0.0.1-SNAPSHOT.jar"]

EXPOSE 80
STOPSIGNAL SIGINT
CMD /bin/boot.sh