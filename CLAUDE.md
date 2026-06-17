# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./gradlew build                  # compile, test, package all modules
./gradlew test                   # run test suite for all modules
./gradlew :server:bootRun        # run the MCP server
./gradlew :client:bootRun        # run the MCP client
./gradlew :server:bootJar        # produce executable server JAR
./gradlew :server:bootBuildImage # build OCI container image for server
```

To run a single test class:
```bash
./gradlew :server:test --tests "com.example.mcp_server.McpServerApplicationTests"
```

## Architecture

This is a **Spring Boot MCP multi-module project** — a server that exposes business logic to LLMs via the [Model Context Protocol](https://modelcontextprotocol.io), and a client that connects to it. The stack is Spring Boot 4.1.0 + Spring AI 2.0.0 + Java 21.

### Modules

| Module | Description |
|---|---|
| `server` | MCP server exposing tools, resources, and prompts over SSE/HTTP (`spring-ai-starter-mcp-server-webmvc`) |
| `client` | MCP client that connects to the server (`spring-ai-starter-mcp-client`) |

### MCP exposure model

All MCP surface area lives in `@Service` beans in the server via Spring AI annotations:

| Annotation | Purpose |
|---|---|
| `@McpTool` / `@McpToolParam` | Exposes a method as a callable tool |
| `@McpResource` | Exposes a static resource at a URI with a MIME type |
| `@McpPrompt` / `@McpArg` | Exposes a reusable prompt template |

Methods annotated this way receive a `McpSyncServerExchange` as their first parameter, which provides bidirectional protocol communication.

### Key files

- [server/src/main/java/com/example/mcp_server/ReservationService.java](server/src/main/java/com/example/mcp_server/ReservationService.java) — the only service; contains all MCP tools (`createReservation`, `listReservations`), resources (`/reservations`), and prompts (`format`). Add new MCP endpoints here or in additional `@Service` beans.
- [server/src/main/java/com/example/mcp_server/McpServerApplication.java](server/src/main/java/com/example/mcp_server/McpServerApplication.java) — standard Spring Boot entry point; no customization needed.
- [client/src/main/java/com/example/mcp_client/McpClientApplication.java](client/src/main/java/com/example/mcp_client/McpClientApplication.java) — standard Spring Boot entry point for the client.
- [server/src/main/resources/application.properties](server/src/main/resources/application.properties) — server config; sets transport to SSE (stdio disabled), `spring.application.name=mcp-server`.
- [client/src/main/resources/application.properties](client/src/main/resources/application.properties) — client config; sets `spring.application.name=mcp-client`.

### Transport

The server runs in **SSE/HTTP mode** via `spring-ai-starter-mcp-server-webmvc` (`spring.ai.mcp.server.stdio=false`). The SSE endpoint defaults to `/sse` and the message endpoint to `/mcp/messages` (commented out in properties but available to configure).
