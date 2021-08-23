package home.budget.assisstant;

import home.budget.assisstant.logic.RegisterAccountService;
import home.budget.assisstant.model.TransferAmount;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class AssistantApplicationTests {

	private final RegisterAccountService registerAccountService;

	@Autowired
	public AssistantApplicationTests(RegisterAccountService registerAccountService) {
		this.registerAccountService = registerAccountService;
	}

	@Test
	void contextLoads() {
	}

	@Test
	void checkIfNegativeValueRechargeCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(-100));

		final Exception exception = assertThrows(ResponseStatusException.class, () -> {
			registerAccountService.rechargeRegisterAccount(1L, transferAmount);
		});

		final String message = exception.getMessage();
		final boolean isMessage = message != null && !message.isEmpty();

		assertTrue(isMessage);
	}

	@Test
	void checkIfRechargeToNonExistingRegisterAccountCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(100));

		final Exception exception = assertThrows(ResponseStatusException.class, () -> {
			registerAccountService.rechargeRegisterAccount(999999L, transferAmount);
		});

		final String message = exception.getMessage();
		final boolean isMessage = message != null && !message.isEmpty();

		assertTrue(isMessage);
	}

}
