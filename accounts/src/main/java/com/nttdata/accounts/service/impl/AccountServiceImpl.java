package com.nttdata.accounts.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nttdata.accounts.domain.dto.AccountDTO;
import com.nttdata.accounts.exception.ResourceFoundException;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.service.ClienteWebService;
import com.nttdata.accounts.service.mapper.AccountMapper;
import com.nttdata.accounts.util.Constants;
import com.nttdata.accounts.util.ResourseApplication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final ClienteWebService clienteService;

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accounts = accountRepository.findByEstado(Constants.ACTIVE_STATUS).stream().map(accountMapper::toAccountDTO).toList();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("accounts.not.found"));
        }
        return accounts;
    }

    @Override
    public AccountDTO getAccountByNumber(String accountNumber) {
        return accountRepository.findByNumeroCuentaAndEstado(accountNumber, Constants.ACTIVE_STATUS).map(accountMapper::toAccountDTO).orElseThrow(()
                -> new ResourceNotFoundException(ResourseApplication.properties.getProperty("account.not.found")));

    }

    @Override
    public AccountDTO saveAccount(AccountDTO account) {
        try {
            clienteService.getClienteById(account.getClienteId()).block();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        accountRepository.findByNumeroCuentaAndEstado(account.getNumeroCuenta(), Constants.ACTIVE_STATUS).ifPresent(res -> {
            throw new ResourceFoundException(ResourseApplication.properties.getProperty("account.exists"));
        });

        return accountMapper.toAccountDTO(accountRepository.save(accountMapper.toAccount(account)));
    }

    @Override
    public AccountDTO updateAccount(String accountNumber, AccountDTO accountToUpdate) {
        AccountDTO account = getAccountByNumber(accountNumber);
        account.setEstado(accountToUpdate.getEstado());
        account.setSaldoInicial(accountToUpdate.getSaldoInicial());
        account.setTipoCuenta(accountToUpdate.getTipoCuenta());
        return accountMapper.toAccountDTO(accountRepository.save(accountMapper.toAccount(account)));
    }

    @Override

    public AccountDTO deleteAccount(String accountNumber) {
        AccountDTO account = getAccountByNumber(accountNumber);
        account.setEstado(Constants.DELETED_STATUS);
        return accountMapper.toAccountDTO(accountRepository.save(accountMapper.toAccount(account)));
    }
}
