package home.budget.assisstant.model;

import java.math.BigDecimal;

public class TransferAmount {

    private BigDecimal transferValue;

    public TransferAmount(BigDecimal transferValue) {
        this.transferValue = transferValue;
    }

    public TransferAmount() {
    }

    public BigDecimal getTransferValue() {
        return transferValue;
    }


}
