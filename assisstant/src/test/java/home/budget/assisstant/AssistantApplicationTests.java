package home.budget.assisstant;

import home.budget.assisstant.logic.RegisterAccountService;
import home.budget.assisstant.model.RegisterAccount;
import home.budget.assisstant.model.TransferAmount;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

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

		final String failedTestMessage =
				"Expected to get an exception not allowing for a negative value recharge";

		assertTrue(isMessage, failedTestMessage);
	}

	@Test
	void checkIfRechargeToNonExistingRegisterAccountCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(100));

		final Exception exception = assertThrows(ResponseStatusException.class, () -> {
			registerAccountService.rechargeRegisterAccount(999999L, transferAmount);
		});

		final String message = exception.getMessage();
		final boolean isMessage = message != null && !message.isEmpty();

		final String failedTestMessage =
				"Expected to get an exception not allowing for a recharge of nonexistent register account";

		assertTrue(isMessage, failedTestMessage);
	}

	@Test
	void checkIfNegativeValueTransferCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(-100));

		final Exception exception = assertThrows(ResponseStatusException.class, () -> {
			registerAccountService.transferMoney(1L, 4L, transferAmount);
		});

		final String message = exception.getMessage();
		final boolean isMessage = message != null && !message.isEmpty();

		final String failedTestMessage =
				"Expected to get an exception not allowing for a negative value transfer";

		assertTrue(isMessage, failedTestMessage);
	}

	@Test
	void checkIfTransferFromNonExistingRegisterAccountCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(100));

		final Exception exception = assertThrows(ResponseStatusException.class, () -> {
			registerAccountService.transferMoney(777777L, 4L, transferAmount);
		});

		final String message = exception.getMessage();
		final boolean isMessage = message != null && !message.isEmpty();

		final String failedTestMessage =
				"Expected to get an exception not allowing for transfer from nonexistent register account";

		assertTrue(isMessage, failedTestMessage);
	}

	@Test
	void checkIfTransferToNonExistingRegisterAccountCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(100));

		final Exception exception = assertThrows(ResponseStatusException.class, () -> {
			registerAccountService.transferMoney(1L, 888888L, transferAmount);
		});

		final String message = exception.getMessage();
		final boolean isMessage = message != null && !message.isEmpty();

		final String failedTestMessage =
				"Expected to get an exception not allowing for transfer to nonexistent register account";

		assertTrue(isMessage, failedTestMessage);
	}

	@Test
	void checkIfValidTransferCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(200));

		registerAccountService.transferMoney(1L, 4L, transferAmount);

		final List<RegisterAccount> registerAccounts = registerAccountService.getRegisterAccounts();

		final RegisterAccount walletRegisterAccount = registerAccounts
				.stream()
				.filter(x -> "Wallet".equals(x.getName())).findFirst()
				.orElseGet(RegisterAccount::new);

		final RegisterAccount foodRegisterAccount = registerAccounts
				.stream()
				.filter(x -> "Food expenses".equals(x.getName())).findFirst()
				.orElseGet(RegisterAccount::new);

		final String walletName = walletRegisterAccount.getName();
		final BigDecimal walletBalance = walletRegisterAccount.getBalance();

		final String foodName = foodRegisterAccount.getName();
		final BigDecimal foodBalance = foodRegisterAccount.getBalance();

		final boolean isWalletBalanceDecreased = walletName != null && walletBalance.compareTo(BigDecimal.valueOf(800)) == 0;
		final boolean isFoodBalanceIncreased = foodName != null && foodBalance.compareTo(BigDecimal.valueOf(200)) == 0;

		final String message = "Wallet and/or food balances should have other values";

		assertTrue(isWalletBalanceDecreased && isFoodBalanceIncreased, message);

//		restore balances to initial values so as not to change DB entries, because the same test next time would fail
		registerAccountService.transferMoney(4L, 1L, transferAmount);

	}

}
