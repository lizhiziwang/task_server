FROM 10.0.0.253:9090/public/anso-alpine-jdk8:v3
WORKDIR /opt
ADD ./pipe-center-service/target/*.jar ./app.jar
ADD ./README.txt ./README.txt
ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom -jar app.jar"]