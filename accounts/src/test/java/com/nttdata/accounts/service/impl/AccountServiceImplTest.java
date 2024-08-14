package com.nttdata.accounts.service.impl;

import java.util.Arrays;
import java.util.List;

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

import com.nttdata.accounts.domain.entity.Account;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.util.Constants;
import com.nttdata.accounts.util.ResourseApplication;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

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
        Account account2 = new Account();
        account2.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByEstado(Constants.ACTIVE_STATUS)).thenReturn(Arrays.asList(account1, account2));

        List<Account> accounts = accountService.getAllAccounts();

        assertEquals(2, accounts.size());
        verify(accountRepository, times(1)).findByEstado(Constants.ACTIVE_STATUS);
    }

    @Test
    public void testGetAllAccountsNotFound() {
        when(accountRepository.findByEstado(Constants.ACTIVE_STATUS)).thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAllAccounts();
        });

        assertEquals("No accounts found", exception.getMessage());
    }

    @Test
    public void testGetAccountById() {
        Account account = new Account();
        account.setId(1L);
        account.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(account);

        Account foundAccount = accountService.getAccountById(1L);

        assertNotNull(foundAccount);
        assertEquals(1L, foundAccount.getId());
        verify(accountRepository, times(1)).findByIdAndEstado(1L, Constants.ACTIVE_STATUS);
    }

    @Test
    public void testGetAccountByIdNotFound() {
        when(accountRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(1L);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testSaveAccount() {
        Account account = new Account();
        account.setNumeroCuenta("1234567890");
        account.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(null);
        when(accountRepository.save(account)).thenReturn(account);

        Account savedAccount = accountService.saveAccount(account);

        assertNotNull(savedAccount);
        assertEquals("1234567890", savedAccount.getNumeroCuenta());
        verify(accountRepository, times(1)).findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testSaveAccountAlreadyExists() {
        Account account = new Account();
        account.setNumeroCuenta("1234567890");
        account.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByNumeroCuentaAndEstado("1234567890", Constants.ACTIVE_STATUS)).thenReturn(account);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.saveAccount(account);
        });

        assertEquals("Account already exists", exception.getMessage());
    }

    @Test
    public void testUpdateAccount() {
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setNumeroCuenta("1234567890");
        existingAccount.setClienteId(1L);
        existingAccount.setEstado(Constants.ACTIVE_STATUS);

        Account updatedAccount = new Account();
        updatedAccount.setNumeroCuenta("1234567890");
        updatedAccount.setClienteId(1L);
        updatedAccount.setEstado(Constants.ACTIVE_STATUS);
        updatedAccount.setSaldoInicial(1000.0);
        updatedAccount.setTipoCuenta("Corriente");

        when(accountRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(existingAccount);
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account result = accountService.updateAccount(1L, updatedAccount);

        assertNotNull(result);
        assertEquals(1000.0, result.getSaldoInicial());
        assertEquals("Corriente", result.getTipoCuenta());
        verify(accountRepository, times(1)).findByIdAndEstado(1L, Constants.ACTIVE_STATUS);
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    public void testUpdateAccountNumberError() {
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setNumeroCuenta("1234567890");
        existingAccount.setClienteId(1L);
        existingAccount.setEstado(Constants.ACTIVE_STATUS);

        Account updatedAccount = new Account();
        updatedAccount.setNumeroCuenta("0987654321");
        updatedAccount.setClienteId(1L);
        updatedAccount.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(existingAccount);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.updateAccount(1L, updatedAccount);
        });

        assertEquals("Account number cannot be changed", exception.getMessage());
    }

    @Test
    public void testUpdateAccountClientError() {
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setNumeroCuenta("1234567890");
        existingAccount.setClienteId(1L);
        existingAccount.setEstado(Constants.ACTIVE_STATUS);

        Account updatedAccount = new Account();
        updatedAccount.setNumeroCuenta("1234567890");
        updatedAccount.setClienteId(2L);
        updatedAccount.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(existingAccount);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.updateAccount(1L, updatedAccount);
        });

        assertEquals("Client ID cannot be changed", exception.getMessage());
    }

    @Test
    public void testDeleteAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setEstado(Constants.ACTIVE_STATUS);

        when(accountRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        Account deletedAccount = accountService.deleteAccount(1L);

        assertNotNull(deletedAccount);
        assertEquals(Constants.DELETED_STATUS, deletedAccount.getEstado());
        verify(accountRepository, times(1)).findByIdAndEstado(1L, Constants.ACTIVE_STATUS);
        verify(accountRepository, times(1)).save(account);
    }
}
