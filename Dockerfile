ARG JAVA_VERSION=17
FROM amazoncorretto:${JAVA_VERSION} as Build
COPY . /workspace
WORKDIR /workspace

RUN ./gradlew --no-daemon build

# Run spring boot in Docker
FROM amazoncorretto:${JAVA_VERSION} as platform
ARG BUILD_JAR_PATH=build/libs/bazaar-api-0.0.1-SNAPSHOT.jar
COPY --from=Build /workspace/${BUILD_JAR_PATH} .

ENV PORT 8080
EXPOSE $PORT

ENTRYPOINT ["java", "-Xms1024m", "-Dspring.profiles.active=local", "-jar", "-Dserver.port=${PORT}", "bazaar-api-0.0.1-SNAPSHOT.jar"]
