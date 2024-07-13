package com.nttdata.accounts.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.accounts.entity.Account;
import com.nttdata.accounts.entity.Movement;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.repository.MovementRepository;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.service.MovementService;
import com.nttdata.accounts.util.ResourseApplication;

@Service
public class MovementServiceImpl implements MovementService {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public List<Movement> getAllMovements() {
        List<Movement> movements = movementRepository.findAll();
        if (movements.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("movements.not.found"));
        }
        return movements;
    }

    @Override
    public Optional<Movement> getMovementById(Long id) {
        Optional<Movement> movement = movementRepository.findById(id);
        if (movement.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("movement.not.found"));
        }
        return movement;
    }

    @Override
    public Movement saveMovement(Movement movement) {
        Account account = accountService.getAccountById(movement.getCuenta().getId());
        validateBalance(account.getSaldoInicial(), movement.getValor());
        Double newBalance = calculateBalance(account.getSaldoInicial(), movement.getValor());

        movement.setFecha(new Date());
        movement.setDescripcion(createDescription(movement));
        movement.setTipoMovimiento(account.getTipoCuenta());
        movement.setSaldoInicial(account.getSaldoInicial());
        movement.setSaldoFinal(newBalance);

        account.setSaldoInicial(newBalance);
        accountRepository.save(account);

        return movementRepository.save(movement);
    }

    @Override
    public void validateBalance(Double accountBalance, Double amount) {
        if (accountBalance + amount < 0) {
            throw new RuntimeException(ResourseApplication.properties.getProperty("balance.not.avaliable"));
        }
    }

    @Override
    public Double calculateBalance(Double accountBalance, Double amount) {
        return accountBalance + amount;
    }

    public String createDescription(Movement movement) {
        return movement.getValor() > 0
                ? ResourseApplication.properties.getProperty("balance.positive") + " " + String.valueOf(movement.getValor())
                : ResourseApplication.properties.getProperty("balance.negative") + " " + String.valueOf(movement.getValor());
    }

}
