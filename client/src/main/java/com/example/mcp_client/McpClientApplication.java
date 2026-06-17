package com.example.mcp_client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpClientApplication.class, args);
	}

	@Bean
	public ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner demo(ChatClient chatClient, ToolCallbackProvider mcpTools) {
		return args -> {
			String response = chatClient
					.prompt("Make me a reservation for 3pm tomorrow and then list my reservations.")
					.tools(mcpTools)
					.call()
					.content();
			System.out.println(response);
		};
	}

}
