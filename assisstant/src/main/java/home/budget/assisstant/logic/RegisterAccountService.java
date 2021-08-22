package home.budget.assisstant.logic;

import home.budget.assisstant.model.RechargeAmount;
import home.budget.assisstant.model.RegisterAccount;
import home.budget.assisstant.repository.RegisterAccountRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        if (dbRegisterAccount.isEmpty()) {
            final String message = String.format("Register account with id: %s does not exist.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        final RegisterAccount registerAccount = dbRegisterAccount.get();
        final BigDecimal previousBalance = registerAccount.getBalance();

        BigDecimal rechargeValue = rechargeAmount.getRechargeValue();

        final BigDecimal currentBalance = previousBalance.add(rechargeValue);
        registerAccount.setBalance(currentBalance);

        return registerAccountRepository.save(registerAccount);
    }
}
