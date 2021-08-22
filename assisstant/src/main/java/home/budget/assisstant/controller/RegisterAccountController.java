package home.budget.assisstant.controller;

import home.budget.assisstant.logic.RegisterAccountService;
import home.budget.assisstant.model.RegisterAccount;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
