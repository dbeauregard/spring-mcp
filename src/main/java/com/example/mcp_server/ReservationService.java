package com.example.mcp_server;

import java.util.List;

import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpProgressToken;
import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import io.modelcontextprotocol.server.McpSyncServerExchange;

@Service
public class ReservationService {

    @McpTool(description = "Create a Reservation")
    public String createReservation(McpSyncServerExchange exchange,
            @McpToolParam(description = "date and time") String input) {

        return "Reservation created for " + input;
    }

    @McpTool(description = "List Reservations")
    public String listReservations(McpSyncServerExchange exchange) {

        return "Reservations: a, b, c ";
    }

    @McpResource(description = "Get types of reservations", uri = "/reservations", mimeType = "text/plain")
    public List<String> myResource() {
        return List.of("One", "Two");
    }

    @McpPrompt(name = "format", description = "Rewrites the contents of the document in Markdown format.")
    public String myPrompt(@McpArg String myArg) {
        String prompt = "Something %s".formatted(myArg);
        return prompt;
    }

}
