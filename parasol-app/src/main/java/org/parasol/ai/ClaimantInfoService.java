package org.parasol.ai;

import jakarta.enterprise.context.ApplicationScoped;

import org.parasol.model.Claimant;
import org.parasol.model.claim.Claim;

import io.quarkus.logging.Log;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

@ApplicationScoped
public class ClaimantInfoService {
	@Tool("Gets information about a claimant given the claim id")
	public Claimant getClaimant(@P("The claim id") long claimId) {
		Log.infof("Getting claimant for claim id %d", claimId);
		return Claim.find("id", Long.valueOf(claimId))
			.project(Claimant.class)
			.firstResultOptional()
			.orElseThrow(() -> new IllegalArgumentException("Claimant not found for claim id %d".formatted(claimId)));
	}

	@Tool("Gets the from email address for any outgoing emails")
	public String getFromEmailAddress() {
		return "claims@parasol.com";
	}
}
