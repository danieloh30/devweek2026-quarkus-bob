package org.parasol.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.parasol.model.Claimant;
import org.parasol.model.claim.Claim;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestTransaction
class ClaimantInfoServiceTests {
	@Inject
	ClaimantInfoService claimantInfoService;

	@Test
	void findClaimantFound() {
		Claim.deleteAll();
		assertThat(Claim.count()).isZero();

		var claim = new Claim();
		claim.claimNumber = "CLM1234";
		claim.category = "Auto";
		claim.policyNumber = "123456789";
		claim.inceptionDate = LocalDate.now();
		claim.clientName = "Marty McFly";
		claim.subject = "collision";
		claim.body = "body";
		claim.location = "driveway";
		claim.time = Instant.now().toString();
		claim.sentiment = "Very bad";
		claim.emailAddress = "martymcfly@email.com";
		claim.status = "Under investigation";

		Claim.persist(claim);
		assertThat(Claim.count()).isOne();

		assertThat(this.claimantInfoService.getClaimant(claim.id))
			.isNotNull()
			.usingRecursiveComparison()
			.isEqualTo(new Claimant(claim.clientName, claim.emailAddress));
	}

	@Test
	void findClaimantNotFound() {
		Claim.deleteAll();
		assertThat(Claim.count()).isZero();

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> this.claimantInfoService.getClaimant(1L))
			.withMessage("Claimant not found for claim id 1");
	}
}