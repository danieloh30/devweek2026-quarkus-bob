# Parasol — Demo

This repository contains two Quarkus-based projects that work together:

- [`mcp-server`](mcp-server) — A Model Context Protocol (MCP) Tool Server that exposes tools the app can call during reasoning. Runs on port 8088.
- [`parasol-app`](parasol-app) — The main application demonstrating AI-assisted claims processing with chat/RAG, web sockets, email, observability, and database integration. It connects to the MCP Tool Server. Runs on port 8080 by default.

Both projects are standard Maven builds that you run separately in dev mode.

## How the projects interact

[`parasol-app`](parasol-app) uses LangChain4j’s MCP client to call tools provided by [`mcp-server`](mcp-server) over HTTP.

- [`parasol-app`](parasol-app) is configured to reach the MCP endpoint at http://localhost:8088/mcp (see [`parasol-app`](parasol-app)`/src/main/resources/application.yml` → `quarkus.langchain4j.mcp.tools`).
- You must start [`mcp-server`](mcp-server) first (`./mvnw quarkus:dev`), then start [`parasol-app`](parasol-app) (`./mvnw quarkus:dev`) so the tool list/handshake succeeds.
    - Make sure to have the `OPENAI_API_KEY` environment variable set to your OpenAI API key.
    - The observability stack (Grafana/Tempo/Loki/etc) is shared between the two. 

## High-level demo flow

1) User interacts with [`parasol-app`](parasol-app)
    - [Go to a claim](http://localhost:8080/ClaimDetail/1), open the chatbot, and ask something like `Please send an email to the claimant notifying them that adjuster Emmet Brown has been assigned to the claim.`
2) The bot may say that it sent the email. If so, ask it if it _really_ sent it. It should say it doesn't know how to do that.
3) Open [`SendEmailService`](mcp-server/src/main/java/org/parasol/tools/SendEmailService.java) and uncomment the line `@dev.langchain4j.agent.tool.Tool(name = "sendEmail", value = "Sends an email")`
4) In the `mcp-server` dev mode, hit the `s` key to trigger a reload.
5) In the `parasol-app` dev mode, hit the `s` key to trigger a reload.
6) Return to the chatbot and refresh the browser page, the re-ask `Please send an email to the claimant notifying them that adjuster Emmet Brown has been assigned to the claim.`
7) Go to the [mcp-server mailpit dev UI](http://localhost:8088/q/dev-ui/quarkus-mailpit/mailpit-ui) and see the email sent.

## Prerequisites

Ensure the following are installed and available:

- Java 21+
- Maven 3.9+
- Docker or Podman (for Quarkus Dev Services: Postgres, Mailpit, LGTM, etc.)

Environment variables:
- `OPENAI_API_KEY` — required if using OpenAI (default config). Not needed when using the Ollama profiles.

## Useful URLs
- [`parasol-app`](parasol-app) Dev UI: http://localhost:8080/q/dev
- [`parasol-app`](parasol-app) OpenAPI/Swagger UI: http://localhost:8080/q/swagger-ui
- MCP Tool Server base: http://localhost:8088/mcp

Notes:
- In dev mode, Quarkus Dev Services will automatically start containers for dependencies (e.g., Postgres, Mailpit, LGTM) if Docker/Podman is available.
- The [`parasol-app`](parasol-app) uses Quinoa to build the front-end automatically during dev; Node/npm are provisioned as specified in application.yml.
