# AI-Powered Cloud-Native Test Automation Platform

Java + Playwright test framework with Docker, Kubernetes, Jenkins, SonarQube, Prometheus, Grafana and AI-based failure triage.

## Run tests locally
```bash
cd tests
mvn test                          # all tests, headless
mvn test -Dgroups=smoke           # smoke only
mvn test -Dheadless=false         # watch the browser
```
Traces are saved in `tests/target/traces/` (open with `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace <file>"`).

## Run in Docker
```bash
docker build -t qa-tests .
docker run --rm qa-tests
```
Automatic Jenkins CI trigger test
