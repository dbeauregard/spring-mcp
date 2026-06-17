# spring-mcp

A Spring Boot implementation of the [Model Context Protocol (MCP)](https://modelcontextprotocol.io), consisting of a server that exposes business logic to LLMs and a client that connects to it.

**Stack:** Spring Boot 4.1.0 · Spring AI 2.0.0 · Java 21

## Modules

| Module | Description |
|---|---|
| `server` | Exposes MCP tools, resources, and prompts over SSE/HTTP |
| `client` | Connects to the MCP server |

## Getting Started

### Prerequisites

- Java 21+

### Build & Run

```bash
# Build all modules
./gradlew build

# Run the server
./gradlew :server:bootRun

# Run the client
./gradlew :client:bootRun
```

### Packaging

```bash
./gradlew :server:bootJar        # executable JAR
./gradlew :server:bootBuildImage # OCI container image
```

## Transport

The server uses **SSE/HTTP** transport (`spring-ai-starter-mcp-server-webmvc`). By default:

- SSE endpoint: `/sse`
- Message endpoint: `/mcp/messages`

These can be customized in [server/src/main/resources/application.properties](server/src/main/resources/application.properties).

## MCP Surface Area

Tools, resources, and prompts are defined in `@Service` beans using Spring AI annotations:

| Annotation | Purpose |
|---|---|
| `@McpTool` / `@McpToolParam` | Callable tool |
| `@McpResource` | Static resource at a URI |
| `@McpPrompt` / `@McpArg` | Reusable prompt template |

The current implementation in [`ReservationService`](server/src/main/java/com/example/mcp_server/ReservationService.java) exposes:

- **Tools:** `createReservation`, `listReservations`
- **Resources:** `/reservations`
- **Prompts:** `format`

New MCP endpoints can be added to `ReservationService` or in additional `@Service` beans.
