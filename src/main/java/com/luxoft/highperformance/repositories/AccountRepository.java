package com.luxoft.highperformance.repositories;

import com.luxoft.highperformance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
}
