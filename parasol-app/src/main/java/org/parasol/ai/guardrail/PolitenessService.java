package org.parasol.ai.guardrail;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.mcp.runtime.McpToolBox;

@RegisterAiService
public interface PolitenessService {
	@McpToolBox("tools")
	boolean isPolite(String query);
}
