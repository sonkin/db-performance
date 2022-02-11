package com.luxoft.highperformance.service;

import com.luxoft.highperformance.model.Account;
import com.luxoft.highperformance.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    AtomicInteger withdraws = new AtomicInteger();
    AtomicInteger deposits = new AtomicInteger();
    AtomicInteger amountVar = new AtomicInteger();

    public void createAccount1() {
        Account account = new Account(1, 0);
        accountRepository.save(account);
    }

    public Account getAccount1() {
        return accountRepository.findById(1L).get();
    }

    @Transactional
    public void deposit() {
        Account account = getAccount1();
        long amount = account.getAmount();
        account.setAmount(amount+1);
        deposits.incrementAndGet();
        accountRepository.save(account);
        amountVar.incrementAndGet();
    }

    @Transactional
    public void withdraw() {
        Account account = getAccount1();
        long amount = account.getAmount();
        account.setAmount(amount-1);
        withdraws.incrementAndGet();
        accountRepository.save(account);
        amountVar.addAndGet(-1);
    }

    @Transactional
    public void resetAccount() {
        Account account = getAccount1();
        account.setAmount(0);
        accountRepository.save(account);
    }

    public String getStat() {
        return deposits.get()+" : "+withdraws.get()+" : "+amountVar.get();
    }

}
