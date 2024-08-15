package com.nttdata.accounts.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.nttdata.accounts.domain.dto.AccountDTO;
import com.nttdata.accounts.domain.dto.MovementDTO;
import com.nttdata.accounts.domain.entity.Account;
import com.nttdata.accounts.domain.entity.Movement;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.repository.MovementRepository;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.service.mapper.AccountMapper;
import com.nttdata.accounts.service.mapper.MovementMapper;
import com.nttdata.accounts.util.ResourseApplication;

public class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MovementMapper movementMapper;

    @Mock
    private AccountMapper accountMapper;

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
        MovementDTO movementDTO1 = new MovementDTO();
        MovementDTO movementDTO2 = new MovementDTO();

        when(movementRepository.findAll()).thenReturn(Arrays.asList(new Movement(), new Movement()));
        when(movementMapper.toMovementDTO(any(Movement.class))).thenReturn(movementDTO1, movementDTO2);

        List<MovementDTO> movements = movementService.getAllMovements();

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
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setId(1L);

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
        when(movementMapper.toMovementDTO(movement)).thenReturn(movementDTO);

        MovementDTO foundMovement = movementService.getMovementById(1L);

        assertNotNull(foundMovement);
        assertEquals(1L, foundMovement.getId());
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
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumeroCuenta("123");
        accountDTO.setSaldoInicial(1000.0);

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setCuenta(accountDTO);
        movementDTO.setValor(500.0);
        movementDTO.setFecha(new Date());

        Account account = new Account();
        Movement movement = new Movement();

        when(accountService.getAccountByNumber("123")).thenReturn(accountDTO);
        when(accountMapper.toAccount(accountDTO)).thenReturn(account);
        when(movementMapper.toMovement(movementDTO)).thenReturn(movement);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(movementMapper.toMovementDTO(movement)).thenReturn(movementDTO);
        when(accountRepository.save(account)).thenReturn(account);

        MovementDTO savedMovement = movementService.saveMovement(movementDTO);

        assertNotNull(savedMovement);
        assertEquals(1500.0, savedMovement.getSaldoFinal());
        verify(accountService, times(1)).getAccountByNumber("123");
        verify(accountRepository, times(1)).save(account);
        verify(movementRepository, times(1)).save(movement);
    }

    @Test
    public void testSaveMovementInsufficientBalance() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumeroCuenta("123");
        accountDTO.setSaldoInicial(100.0);

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setCuenta(accountDTO);
        movementDTO.setValor(-200.0);

        when(accountService.getAccountByNumber("123")).thenReturn(accountDTO);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            movementService.saveMovement(movementDTO);
        });

        assertEquals("Insufficient balance", exception.getMessage());
        verify(accountService, times(1)).getAccountByNumber("123");
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
        MovementDTO positiveMovement = new MovementDTO();
        positiveMovement.setValor(100.0);

        MovementDTO negativeMovement = new MovementDTO();
        negativeMovement.setValor(-100.0);

        String positiveDescription = movementService.createDescription(positiveMovement);
        String negativeDescription = movementService.createDescription(negativeMovement);

        assertEquals("Deposit 100.0", positiveDescription);
        assertEquals("Withdrawal -100.0", negativeDescription);
    }
}
