package com.nttdata.accounts.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.nttdata.accounts.entity.Account;
import com.nttdata.accounts.entity.Movement;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.repository.MovementRepository;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.util.ResourseApplication;

public class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private MovementServiceImpl movementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ResourseApplication.properties.setProperty("movements.not.found", "No movements found");
        ResourseApplication.properties.setProperty("movement.not.found", "Movement not found");
        ResourseApplication.properties.setProperty("balance.not.avaliable", "Insufficient balance");
        ResourseApplication.properties.setProperty("balance.positive", "Deposit");
        ResourseApplication.properties.setProperty("balance.negative", "Withdrawal");
    }

    @Test
    public void testGetAllMovements() {
        Movement movement1 = new Movement();
        Movement movement2 = new Movement();

        when(movementRepository.findAll()).thenReturn(Arrays.asList(movement1, movement2));

        List<Movement> movements = movementService.getAllMovements();

        assertEquals(2, movements.size());
        verify(movementRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllMovementsNotFound() {
        when(movementRepository.findAll()).thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            movementService.getAllMovements();
        });

        assertEquals("No movements found", exception.getMessage());
    }

    @Test
    public void testGetMovementById() {
        Movement movement = new Movement();
        movement.setId(1L);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));

        Optional<Movement> foundMovement = movementService.getMovementById(1L);

        assertTrue(foundMovement.isPresent());
        assertEquals(1L, foundMovement.get().getId());
        verify(movementRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMovementByIdNotFound() {
        when(movementRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            movementService.getMovementById(1L);
        });

        assertEquals("Movement not found", exception.getMessage());
    }

    @Test
    public void testSaveMovement() {
        Account account = new Account();
        account.setId(1L);
        account.setSaldoInicial(1000.0);

        Movement movement = new Movement();
        movement.setCuenta(account);
        movement.setValor(500.0);

        when(accountService.getAccountById(1L)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(movementRepository.save(movement)).thenReturn(movement);

        Movement savedMovement = movementService.saveMovement(movement);

        assertNotNull(savedMovement);
        assertEquals(1500.0, savedMovement.getSaldoFinal());
        verify(accountService, times(1)).getAccountById(1L);
        verify(accountRepository, times(1)).save(account);
        verify(movementRepository, times(1)).save(movement);
    }

    @Test
    public void testSaveMovementInsufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setSaldoInicial(100.0);

        Movement movement = new Movement();
        movement.setCuenta(account);
        movement.setValor(-200.0);

        when(accountService.getAccountById(1L)).thenReturn(account);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            movementService.saveMovement(movement);
        });

        assertEquals("Insufficient balance", exception.getMessage());
        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    public void testValidateBalance() {
        assertDoesNotThrow(() -> movementService.validateBalance(100.0, 50.0));
    }

    @Test
    public void testCalculateBalance() {
        assertEquals(150.0, movementService.calculateBalance(100.0, 50.0));
        assertEquals(-100.0, movementService.calculateBalance(100.0, -200.0));
    }

    @Test
    public void testCreateDescription() {
        Movement positiveMovement = new Movement();
        positiveMovement.setValor(100.0);

        Movement negativeMovement = new Movement();
        negativeMovement.setValor(-100.0);

        String positiveDescription = movementService.createDescription(positiveMovement);
        String negativeDescription = movementService.createDescription(negativeMovement);

        assertEquals("Deposit 100.0", positiveDescription);
        assertEquals("Withdrawal -100.0", negativeDescription);
    }
}
