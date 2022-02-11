package com.luxoft.highperformance.controllers;

import com.luxoft.highperformance.model.Account;
import com.luxoft.highperformance.model.Book;
import com.luxoft.highperformance.repositories.AccountRepository;
import com.luxoft.highperformance.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    AtomicInteger failedDeposits = new AtomicInteger();
    AtomicInteger failedWithdraws = new AtomicInteger();

    @PostConstruct
    public void init() {
        accountService.createAccount1();
    }

    @GetMapping("/deposit")
    public void deposit() {
        accountService.deposit();
    }

    @GetMapping("/reset")
    public void reset() {
        accountService.resetAccount();
    }

    @GetMapping("/withdraw")
    public void withdraw() {
        accountService.withdraw();
    }

    @GetMapping("/account")
    public Account getAccount1() {
        Account account1 = accountService.getAccount1();
        return account1;
    }

    @GetMapping("/account-stat")
    public String getAccountStat() {
        return accountService.getStat();
    }

    @GetMapping("/account-failures")
    public String getAccountFailures() {
        return "Failed Deposits: "+failedDeposits.get()+"\n"+
                "Failed Withdraws: "+failedWithdraws.get();
    }

}
