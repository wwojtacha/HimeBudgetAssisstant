package home.budget.assisstant.logic;

import home.budget.assisstant.model.RegisterAccount;
import home.budget.assisstant.model.TransferAmount;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransferValidator {

    void validateTransferAmountCorrectness(final TransferAmount transferAmount) {
        final BigDecimal transferValue = transferAmount.getTransferValue();

        if (transferValue != null && transferValue.compareTo(BigDecimal.ZERO) < 0) {
            final String message = "Transfer/recharge amount must not be a negative number.";
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, message);
        }
    }

    void validateRegisterAccountPresence(Long id, Optional<RegisterAccount> dbRegisterAccount) {
        if (dbRegisterAccount.isEmpty()) {
            final String message = String.format("Register account with id: %s does not exist.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
    }

    void validateAvailableBalance(final BigDecimal sourceBalance, final BigDecimal transferValue) {
        if (sourceBalance.compareTo(transferValue) < 0) {
            final String message = "Insufficient funds to make the transfer.";
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, message);
        }
    }

}
