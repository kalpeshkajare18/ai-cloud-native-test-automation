FROM mcr.microsoft.com/playwright/java:v1.49.0-noble
WORKDIR /app
COPY tests/pom.xml .
RUN mvn -q dependency:go-offline
COPY tests/src ./src
CMD ["mvn", "test", "-Dheadless=true"]
