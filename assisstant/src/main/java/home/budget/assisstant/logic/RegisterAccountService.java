package home.budget.assisstant.logic;

import home.budget.assisstant.model.TransferAmount;
import home.budget.assisstant.model.RegisterAccount;
import home.budget.assisstant.repository.RegisterAccountRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterAccountService {

    private final RegisterAccountRepository registerAccountRepository;
    private final TransferValidator transferValidator;

    public RegisterAccountService(RegisterAccountRepository registerAccountRepository, TransferValidator transferValidator) {
        this.registerAccountRepository = registerAccountRepository;
        this.transferValidator = transferValidator;
    }

    public List<RegisterAccount> getRegisterAccounts() {
        return registerAccountRepository.findAll(Sort.by(Sort.Order.desc("name")));
    }

    public RegisterAccount rechargeRegisterAccount(final Long id, final TransferAmount transferAmount) {

        transferValidator.validateTransferAmountCorrectness(transferAmount);

        final Optional<RegisterAccount> dbRegisterAccount = registerAccountRepository.findById(id);

        transferValidator.validateRegisterAccountPresence(id, dbRegisterAccount);

        final RegisterAccount registerAccount = dbRegisterAccount.get();
        final BigDecimal previousBalance = registerAccount.getBalance();

        BigDecimal rechargeValue = transferAmount.getTransferValue();

        final BigDecimal currentBalance = previousBalance.add(rechargeValue);
        registerAccount.setBalance(currentBalance);

        return registerAccountRepository.save(registerAccount);
    }

    @Transactional
    public RegisterAccount transferMoney(
            final Long sourceId, final Long destinationId,
            final TransferAmount transferAmount) {

        transferValidator.validateTransferAmountCorrectness(transferAmount);

        final Optional<RegisterAccount> dbSourceRegisterAccount = registerAccountRepository.findById(sourceId);
        transferValidator.validateRegisterAccountPresence(sourceId, dbSourceRegisterAccount);

        final Optional<RegisterAccount> dbDestinationRegisterAccount = registerAccountRepository.findById(destinationId);
        transferValidator.validateRegisterAccountPresence(destinationId, dbDestinationRegisterAccount);

        final RegisterAccount sourceRegisterAccount = dbSourceRegisterAccount.get();
        final RegisterAccount destinationRegisterAccount = dbDestinationRegisterAccount.get();

        final BigDecimal transferValue = transferAmount.getTransferValue();

        final BigDecimal previousSourceBalance = sourceRegisterAccount.getBalance();

        transferValidator.validateAvailableBalance(previousSourceBalance, transferValue);

        final BigDecimal currentSourceBalance = previousSourceBalance.subtract(transferValue);
        sourceRegisterAccount.setBalance(currentSourceBalance);
        registerAccountRepository.save(sourceRegisterAccount);

        final BigDecimal previousDestinationBalance = destinationRegisterAccount.getBalance();
        final BigDecimal currentDestinationBalance = previousDestinationBalance.add(transferValue);
        destinationRegisterAccount.setBalance(currentDestinationBalance);
        registerAccountRepository.save(destinationRegisterAccount);

        return sourceRegisterAccount;
    }

}
