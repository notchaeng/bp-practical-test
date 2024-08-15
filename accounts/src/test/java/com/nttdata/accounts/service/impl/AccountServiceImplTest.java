package com.nttdata.accounts.service.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.nttdata.accounts.domain.dto.AccountDTO;
import com.nttdata.accounts.domain.entity.Account;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.service.mapper.AccountMapper;
import com.nttdata.accounts.util.Constants;
import com.nttdata.accounts.util.ResourseApplication;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ResourseApplication.properties.setProperty("accounts.not.found", "No accounts found");
        ResourseApplication.properties.setProperty("account.not.found", "Account not found");
        ResourseApplication.properties.setProperty("account.exists", "Account already exists");
        ResourseApplication.properties.setProperty("account.number.error", "Account number cannot be changed");
        ResourseApplication.properties.setProperty("account.client.error", "Client ID cannot be changed");
    }

    @Test
    public void testGetAllAccounts() {
        Account account1 = new Account();
        account1.setEstado(Constants.ACTIVE_STATUS);
        AccountDTO accountDTO1 = new AccountDTO();
        when(accountMapper.toAccountDTO(account1)).thenReturn(accountDTO1);

        Account account2 = new Account();
        account2.setEstado(Constants.ACTIVE_STATUS);
        AccountDTO accountDTO2 = new AccountDTO();
        when(accountMapper.toAccountDTO(account2)).thenReturn(accountDTO2);

        when(accountRepository.findByEstado(Constants.ACTIVE_STATUS)).thenReturn(List.of(account1, account2));

        List<AccountDTO> accounts = accountService.getAllAccounts();

        assertEquals(2, accounts.size());
        verify(accountRepository, times(1)).findByEstado(Constants.ACTIVE_STATUS);
    }

    @Test
    public void testGetAllAccountsNotFound() {
        when(accountRepository.findByEstado(Constants.ACTIVE_STATUS)).thenReturn(List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAllAccounts();
        });

        assertEquals("No accounts found", exception.getMessage());
    }

    @Test
    public void testGetAccountByNumber() {
        Account account = new Account();
        account.setNumeroCuenta("1234567890");
        account.setEstado(Constants.ACTIVE_STATUS);
        AccountDTO accountDTO = new AccountDTO();
        when(accountMapper.toAccountDTO(account)).thenReturn(accountDTO);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(account));

        AccountDTO foundAccount = accountService.getAccountByNumber("1234567890");

        assertNotNull(foundAccount);
        verify(accountRepository, times(1)).findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS);
    }

    @Test
    public void testGetAccountByNumberNotFound() {
        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountByNumber("1234567890");
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testSaveAccountAlreadyExists() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumeroCuenta("1234567890");
        accountDTO.setEstado(Constants.ACTIVE_STATUS);

        Account account = new Account();
        when(accountMapper.toAccount(accountDTO)).thenReturn(account);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(account));
    }

    @Test
    public void testUpdateAccount() {
        AccountDTO existingAccountDTO = new AccountDTO();
        existingAccountDTO.setNumeroCuenta("1234567890");
        existingAccountDTO.setEstado(Constants.ACTIVE_STATUS);

        AccountDTO accountToUpdate = new AccountDTO();
        accountToUpdate.setNumeroCuenta("1234567890");
        accountToUpdate.setEstado(Constants.ACTIVE_STATUS);
        accountToUpdate.setSaldoInicial(1000.0);
        accountToUpdate.setTipoCuenta("Corriente");

        Account existingAccount = new Account();
        when(accountMapper.toAccount(existingAccountDTO)).thenReturn(existingAccount);
        when(accountMapper.toAccountDTO(existingAccount)).thenReturn(existingAccountDTO);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        AccountDTO result = accountService.updateAccount("1234567890", accountToUpdate);

        assertNotNull(result);
        assertEquals(1000.0, result.getSaldoInicial());
        assertEquals("Corriente", result.getTipoCuenta());
        verify(accountRepository, times(1)).findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS);
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    public void testUpdateAccountNumberError() {
        AccountDTO existingAccountDTO = new AccountDTO();
        existingAccountDTO.setNumeroCuenta("1234567890");
        existingAccountDTO.setEstado(Constants.ACTIVE_STATUS);

        AccountDTO accountToUpdate = new AccountDTO();
        accountToUpdate.setNumeroCuenta("0987654321");
        accountToUpdate.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(new Account()));
    }

    @Test
    public void testUpdateAccountClientError() {
        AccountDTO existingAccountDTO = new AccountDTO();
        existingAccountDTO.setNumeroCuenta("1234567890");
        existingAccountDTO.setClienteId("1");
        existingAccountDTO.setEstado(Constants.ACTIVE_STATUS);

        AccountDTO accountToUpdate = new AccountDTO();
        accountToUpdate.setNumeroCuenta("1234567890");
        accountToUpdate.setClienteId("2");
        accountToUpdate.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(new Account()));

    }

    @Test
    public void testDeleteAccount() {
        AccountDTO existingAccountDTO = new AccountDTO();
        existingAccountDTO.setNumeroCuenta("1234567890");
        existingAccountDTO.setEstado(Constants.ACTIVE_STATUS);

        Account existingAccount = new Account();
        when(accountMapper.toAccount(existingAccountDTO)).thenReturn(existingAccount);
        when(accountMapper.toAccountDTO(existingAccount)).thenReturn(existingAccountDTO);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        AccountDTO deletedAccount = accountService.deleteAccount("1234567890");

        assertNotNull(deletedAccount);
        assertEquals(Constants.DELETED_STATUS, deletedAccount.getEstado());
        verify(accountRepository, times(1)).findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS);
        verify(accountRepository, times(1)).save(existingAccount);
    }
}
