FROM gradle:7.4-jdk17

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . .

RUN gradle build -x test

RUN mkdir -p /opt/app

COPY /workspace/build/libs/demo-0.0.1-SNAPSHOT.jar /opt/app/demo.jar

RUN adduser --disabled-password --home /app app
USER app

ENTRYPOINT java -Xmx1G -jar /opt/app/demo.jar