package home.budget.assisstant.logic;

import home.budget.assisstant.model.RechargeAmount;
import home.budget.assisstant.model.RegisterAccount;
import home.budget.assisstant.repository.RegisterAccountRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterAccountService {

    private final RegisterAccountRepository registerAccountRepository;

    public RegisterAccountService(RegisterAccountRepository registerAccountRepository) {
        this.registerAccountRepository = registerAccountRepository;
    }

    public List<RegisterAccount> getRegisterAccounts() {
        return registerAccountRepository.findAll(Sort.by(Sort.Order.desc("name")));
    }

    public RegisterAccount rechargeRegisterAccount(final Long id, final RechargeAmount rechargeAmount) {

        final Optional<RegisterAccount> dbRegisterAccount = registerAccountRepository.findById(id);

        validateRegisterAccountPresence(id, dbRegisterAccount);

        final RegisterAccount registerAccount = dbRegisterAccount.get();
        final BigDecimal previousBalance = registerAccount.getBalance();

        BigDecimal rechargeValue = rechargeAmount.getRechargeValue();

        final BigDecimal currentBalance = previousBalance.add(rechargeValue);
        registerAccount.setBalance(currentBalance);

        return registerAccountRepository.save(registerAccount);
    }

    private void validateRegisterAccountPresence(Long id, Optional<RegisterAccount> dbRegisterAccount) {
        if (dbRegisterAccount.isEmpty()) {
            final String message = String.format("Register account with id: %s does not exist.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
    }

    @Transactional
    public RegisterAccount transferMoney(
            final Long sourceId, final Long destinationId,
            final RechargeAmount transferAmount) {

        final Optional<RegisterAccount> dbSourceRegisterAccount = registerAccountRepository.findById(sourceId);
        validateRegisterAccountPresence(sourceId, dbSourceRegisterAccount);

        final Optional<RegisterAccount> dbDestinationRegisterAccount = registerAccountRepository.findById(destinationId);
        validateRegisterAccountPresence(destinationId, dbDestinationRegisterAccount);

        final RegisterAccount sourceRegisterAccount = dbSourceRegisterAccount.get();
        final RegisterAccount destinationRegisterAccount = dbDestinationRegisterAccount.get();

        final BigDecimal transferValue = transferAmount.getRechargeValue();

        final BigDecimal previousSourceBalance = sourceRegisterAccount.getBalance();

        validateAvailableBalance(previousSourceBalance, transferValue);

        final BigDecimal currentSourceBalance = previousSourceBalance.subtract(transferValue);
        sourceRegisterAccount.setBalance(currentSourceBalance);
        registerAccountRepository.save(sourceRegisterAccount);

        final BigDecimal previousDestinationBalance = destinationRegisterAccount.getBalance();
        final BigDecimal currentDestinationBalance = previousDestinationBalance.add(transferValue);
        destinationRegisterAccount.setBalance(currentDestinationBalance);
        registerAccountRepository.save(destinationRegisterAccount);

        return sourceRegisterAccount;
    }

    private void validateAvailableBalance(final BigDecimal sourceBalance, final BigDecimal transferValue) {
        if (sourceBalance.compareTo(transferValue) < 0) {
            final String message = "Insufficient funds to make the transfer.";
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, message);
        }
    }

}
