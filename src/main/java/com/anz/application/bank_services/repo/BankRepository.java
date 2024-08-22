package com.anz.application.bank_services.repo;

import com.anz.application.bank_services.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Account, Long> {
     Account findByAccountNumber(Long id);
}
