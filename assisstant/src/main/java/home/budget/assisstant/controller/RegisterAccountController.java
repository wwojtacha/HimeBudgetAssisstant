package home.budget.assisstant.controller;

import home.budget.assisstant.logic.RegisterAccountService;
import home.budget.assisstant.model.RechargeAmount;
import home.budget.assisstant.model.RegisterAccount;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/register-accounts")
public class RegisterAccountController {

    private final RegisterAccountService registerAccountService;

    public RegisterAccountController(RegisterAccountService registerAccountService) {
        this.registerAccountService = registerAccountService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RegisterAccount> getAllRegisterAccounts() {
        return registerAccountService.getRegisterAccounts();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegisterAccount rechargeRegisterAccount(
            @PathVariable Long id,
            @RequestBody @Valid RechargeAmount rechargeAmount) {
        return registerAccountService.rechargeRegisterAccount(id, rechargeAmount);
    }

    @PutMapping("/{sourceId}/{destinationId}")
    @ResponseStatus(HttpStatus.OK)
    public RegisterAccount rechargeRegisterAccount(
            @PathVariable Long sourceId,
            @PathVariable Long destinationId,
            @RequestBody @Valid RechargeAmount rechargeAmount) {
        return registerAccountService.transferMoney(sourceId, destinationId, rechargeAmount);
    }
}
