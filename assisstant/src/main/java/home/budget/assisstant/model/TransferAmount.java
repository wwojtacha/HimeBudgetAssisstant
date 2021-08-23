package home.budget.assisstant.model;

import java.math.BigDecimal;

public class TransferAmount {

    private final BigDecimal transferValue;

    public TransferAmount(BigDecimal transferValue) {
        this.transferValue = transferValue;
    }

    public BigDecimal getTransferValue() {
        return transferValue;
    }


}
