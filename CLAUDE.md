# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

This is one module of the `sisfin-environment` workspace — see `../CLAUDE.md` for how it relates to `sisfin-maintenance`, `sisfin-transaction`, `sisfin-mysql`, `sisfin-compose`, and `sisfin-config`. This file covers only what's specific to `sisfin-bypass`.

## What this is

A standalone AWS Lambda (Java 17), **not** a Spring Boot app despite living alongside two Spring Boot services and containing an `application.yml`. It has no Spring dependency in `pom.xml` at all — it's a plain `RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>` built with `aws-lambda-java-core`/`aws-lambda-java-events`. It exists to sit behind one AWS API Gateway URL and forward requests to whichever of `sisfin-maintenance` (port 8080) / `sisfin-transaction` (port 8081) they're addressed to, so both backend services can be reached through a single public endpoint without a real gateway/service mesh in front of them.

`src/main/resources/application.yml` and `HELP.md` are leftovers from when this was scaffolded as a Spring Boot project (HELP.md still links Spring Boot Maven plugin docs) — neither is read at runtime; don't take them as evidence of Spring wiring.

## Commands

Built with plain Maven (no `mvnw` wrapper is meaningfully used here beyond what Eclipse generated — `mvn` works the same):

```bash
cd sisfin-bypass
mvn clean package     # shades a fat jar via maven-shade-plugin: target/sisfin-bypass-1.0-SNAPSHOT.jar
mvn test               # runs the JUnit 5 tests
mvn test -Dtest=BypassHandlerTest       # run one test class
mvn test -Dtest=RequestUtilsTest#teste  # run one test method
```

There's no local "run" — deployment is uploading the shaded jar as a Lambda's code and pointing its handler at `br.com.dfdevforge.sisfinbypass.handler.BypassHandler` (Lambda/API Gateway config isn't checked into this repo). There's no Docker setup or `docker-compose.yaml` here, unlike the other two backend services.

## Architecture

- **`handler/BypassHandler`** — the actual Lambda entry point. `handleRequest()` branches on `GET` vs. everything-else-as-`POST`, builds a `java.net.http.HttpClient` request to the target service, and returns its body/status back to API Gateway with `Access-Control-Allow-Origin: *`. `getApplication(path)` routes by substring: if the path contains `"maintenance"` it targets `http://192.168.0.170:8080`, otherwise `http://192.168.0.170:8081` (transaction) — **these host/port values are hardcoded**, not read from `SISFIN_*` env vars like the other services. On `POST`, a `token` query param (if present) is forwarded as a query string to the target. Any exception is caught, stack-printed, and swallowed — the Lambda response is returned effectively empty rather than propagating an error status.
- **`utils/RequestUtils`** — a `json-simple`-based parser over the raw Lambda input JSON (`path`, `httpMethod`, `queryStringParameters`, `body`, `isBase64Encoded`, plus `getApplication()` which takes the first path segment). **Not used by `BypassHandler`**, which parses via `APIGatewayProxyRequestEvent` directly — this class only appears in `RequestUtilsTest`. Treat it as a preexisting alternate/unused utility rather than part of the live request path; if it truly turns out to be dead code, confirm with the user before removing it.

## Tests

`BypassHandlerTest` and `RequestUtilsTest` are exploratory/manual-inspection style (`System.out.println` + a single `assertNotNull`) rather than real behavioral assertions — match that low bar only if extending them; prefer writing real assertions for any new coverage.

## Conventions

- Package/class names are English; log strings and some comments are Portuguese — match the existing convention per file.
- If you change routing/target-host logic in `getApplication()`, consider whether the hardcoded IPs should become env vars (matching `SISFIN_URL_MAINTENANCE` etc. used by `sisfin-transaction`) — check with the user before changing deploy-affecting behavior like this.
