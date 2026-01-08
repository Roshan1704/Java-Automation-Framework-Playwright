FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Install Playwright dependencies
RUN apk add --no-cache \
    libc6-compat \
    libstdc++ \
    gcompat \
    libx11 \
    libxext \
    libxrender

COPY pom.xml .
RUN mvn dependency:download-sources dependency:download-javadoc dependency:resolve

COPY . .

RUN mvn clean package -DskipTests

ENTRYPOINT ["mvn", "clean", "test", "-DsuiteXmlFile=src/test/resources/testng.xml"]
