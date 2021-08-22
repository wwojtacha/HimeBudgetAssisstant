package home.budget.assisstant.logic;

import home.budget.assisstant.model.RegisterAccount;
import home.budget.assisstant.repository.RegisterAccountRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterAccountService {

    private final RegisterAccountRepository registerAccountRepository;

    public RegisterAccountService(RegisterAccountRepository registerAccountRepository) {
        this.registerAccountRepository = registerAccountRepository;
    }

    public List<RegisterAccount> getRegisterAccounts() {
        return registerAccountRepository.findAll(Sort.by(Sort.Order.desc("name")));
    }
}
