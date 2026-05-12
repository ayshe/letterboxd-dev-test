FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /build

COPY letterboxd-dev-test/pom.xml ./
COPY letterboxd-dev-test/src ./src
RUN mvn clean test package

FROM eclipse-temurin:21-jre-alpine

RUN mkdir /app && addgroup -S app && adduser -S app -G app && chown -R app:app /app
WORKDIR /app

COPY --from=build /build/target/url-shortener.jar /app/url-shortener.jar

USER app
EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError -Dserver.tomcat.basedir=/tmp/tomcat"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/url-shortener.jar"]