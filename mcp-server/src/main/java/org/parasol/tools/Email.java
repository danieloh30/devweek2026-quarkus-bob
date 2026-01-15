package org.parasol.tools;

import dev.langchain4j.agent.tool.P;

public record Email(
    @P("Recipient email address") String to,
    @P("Sender email address") String from,
    @P("Email subject line") String subject,
    @P("Email body content") String body
) {
}

// Made with Bob
