FROM openjdk:8-jdk-alpine

MAINTAINER Kuba Wasilewski <k.k.wasilewski@gmail.com>

VOLUME /tmp

EXPOSE 8084

ARG JAR_FILE=target/gateway-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} server_gateway.jar

ENTRYPOINT ["java","-jar","/server_gateway.jar"]
