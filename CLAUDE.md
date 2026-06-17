# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./gradlew build          # compile, test, package
./gradlew test           # run test suite
./gradlew bootRun        # run the application
./gradlew bootJar        # produce executable JAR
./gradlew bootBuildImage # build OCI container image
```

To run a single test class:
```bash
./gradlew test --tests "com.example.mcp_server.McpServerApplicationTests"
```

## Architecture

This is a **Spring Boot MCP Server** — it exposes business logic to LLMs via the [Model Context Protocol](https://modelcontextprotocol.io). The stack is Spring Boot 4.0.6 + Spring AI 2.0.0 MCP Server starter + Java 21.

### MCP exposure model

All MCP surface area lives in `@Service` beans via Spring AI annotations:

| Annotation | Purpose |
|---|---|
| `@McpTool` / `@McpToolParam` | Exposes a method as a callable tool |
| `@McpResource` | Exposes a static resource at a URI with a MIME type |
| `@McpPrompt` / `@McpArg` | Exposes a reusable prompt template |

Methods annotated this way receive a `McpSyncServerExchange` as their first parameter, which provides bidirectional protocol communication.

### Key files

- [ReservationService.java](src/main/java/com/example/mcp_server/ReservationService.java) — the only service; contains all MCP tools (`createReservation`, `listReservations`), resources (`/reservations`), and prompts (`format`). Add new MCP endpoints here or in additional `@Service` beans.
- [McpServerApplication.java](src/main/java/com/example/mcp_server/McpServerApplication.java) — standard Spring Boot entry point; no customization needed.
- [application.yaml](src/main/resources/application.yaml) — disables the banner and console logging; sets `spring.application.name=mcp-server`.

### Transport

The server runs in **stdio** mode by default (standard MCP transport for CLI/agent integration). No HTTP port is exposed unless configured.
