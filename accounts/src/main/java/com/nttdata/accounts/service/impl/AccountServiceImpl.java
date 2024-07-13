package com.nttdata.accounts.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.accounts.entity.Account;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.util.Constants;
import com.nttdata.accounts.util.ResourseApplication;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findByEstado(Constants.ACTIVE_STATUS);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("accounts.not.found"));
        }
        return accounts;
    }

    @Override
    public Account getAccountById(Long id) {
        Account account = accountRepository.findByIdAndEstado(id, Constants.ACTIVE_STATUS);
        if (null == account) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("account.not.found"));
        }
        return account;
    }

    @Override
    public Account saveAccount(Account account) {
        Account validateAccount = accountRepository.findByNumeroCuentaAndEstado(account.getNumeroCuenta(), Constants.ACTIVE_STATUS);
        if (null != validateAccount) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("account.exists"));
        }
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long id, Account updatedAccount) {
        Account account = getAccountById(id);
        if (!account.getNumeroCuenta().trim().equals(updatedAccount.getNumeroCuenta().trim())) {
            throw new RuntimeException(ResourseApplication.properties.getProperty("account.number.error"));
        }

        if (!Objects.equals(account.getClienteId(), updatedAccount.getClienteId())){
            throw new RuntimeException(ResourseApplication.properties.getProperty("account.client.error"));

        }
        account.setEstado(updatedAccount.getEstado());
        account.setSaldoInicial(updatedAccount.getSaldoInicial());
        account.setTipoCuenta(updatedAccount.getTipoCuenta());
        return accountRepository.save(account);
    }

    @Override

    public Account deleteAccount(Long id) {
        Account account = getAccountById(id);
        account.setEstado(Constants.DELETED_STATUS);
        return accountRepository.save(account);
    }
}
