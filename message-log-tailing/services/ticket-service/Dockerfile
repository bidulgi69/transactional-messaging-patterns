FROM openjdk:11.0.1
ADD ./build/libs/*.jar app.jar
RUN mkdir script

ENTRYPOINT ["java", "-jar", "/app.jar"]