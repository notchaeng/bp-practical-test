package com.nttdata.accounts.service;

import java.util.List;

import com.nttdata.accounts.entity.Account;

public interface AccountService {

    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    Account saveAccount(Account account);

    Account updateAccount(Long id, Account updatedAccount);

    Account deleteAccount(Long id);
}
