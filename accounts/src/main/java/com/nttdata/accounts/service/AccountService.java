package com.nttdata.accounts.service;

import java.util.List;

import com.nttdata.accounts.domain.dto.AccountDTO;
public interface AccountService {

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountByNumber(String accountNumber);

    AccountDTO saveAccount(AccountDTO account);

    AccountDTO updateAccount(String accountNumber, AccountDTO accountToUpdate);

    AccountDTO deleteAccount(String accountNumber);
}
