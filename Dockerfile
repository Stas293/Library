FROM maven:3.9.6 AS builder
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean compile assembly:single

FROM openjdk:21
COPY --from=builder /home/app/target/system_library-jar-with-dependencies.jar /usr/local/lib/system_library-jar-with-dependencies.jar
COPY --from=builder /home/app/src/main/webapp /usr/local/lib/src/main/webapp

WORKDIR /usr/local/lib
ENTRYPOINT ["java","-jar","/usr/local/lib/system_library-jar-with-dependencies.jar"]
