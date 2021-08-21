package home.budget.assisstant.repository;

import home.budget.assisstant.model.RegisterAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterAccountRepository extends JpaRepository<RegisterAccount, Long> {
}
