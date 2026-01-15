package org.parasol.tools;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkiverse.mcp.server.ToolResponse;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

@ApplicationScoped
public class PolitenessService {
  private final PolitenessChecker politenessChecker;

  public PolitenessService(PolitenessChecker politenessChecker) {
    this.politenessChecker = politenessChecker;
  }

  @Tool(name = "isPolite", value = "Checks whether or not some content is considered polite")
  public ToolResponse isPolite(@P("The text to check for politeness") String query) {
    return ToolResponse.success(String.valueOf(this.politenessChecker.isPolite(query)));
  }
}
