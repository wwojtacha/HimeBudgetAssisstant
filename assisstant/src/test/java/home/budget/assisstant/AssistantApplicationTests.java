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

import static org.junit.jupiter.api.Assertions.*;

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
	void checkIfValidRechargeCanBeMade() {
		final TransferAmount transferAmount = new TransferAmount(BigDecimal.valueOf(500));

		final List<RegisterAccount> registerAccountsBeforeRecharge = registerAccountService.getRegisterAccounts();

		final BigDecimal balanceBeforeRecharge = registerAccountsBeforeRecharge.stream()
				.filter(a -> "Wallet".equals(a.getName()))
				.map(RegisterAccount::getBalance)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("'Wallet' account does not exist"));

		registerAccountService.rechargeRegisterAccount(1L, transferAmount);

		final List<RegisterAccount> registerAccountsAfterRecharge = registerAccountService.getRegisterAccounts();

		final BigDecimal balanceAfterRecharge = registerAccountsAfterRecharge.stream()
				.filter(a -> "Wallet".equals(a.getName()))
				.map(RegisterAccount::getBalance)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("'Wallet' account does not exist"));

		final boolean hasBalanceIncreased = balanceAfterRecharge.compareTo(balanceBeforeRecharge) > 0;

		final String message = "Expected to get recharged (increased) value for 'Wallet' balance";

		assertTrue(hasBalanceIncreased, message);
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
	}

	@Test
	void checkRegisterAccountList() {
		final List<RegisterAccount> registerAccounts = registerAccountService.getRegisterAccounts();

		final String message = "Expected to get at least one register account";

		assertFalse(registerAccounts.isEmpty(), message);
	}

}
