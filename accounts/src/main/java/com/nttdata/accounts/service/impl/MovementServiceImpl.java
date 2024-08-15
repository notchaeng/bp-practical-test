package com.nttdata.accounts.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nttdata.accounts.domain.dto.AccountDTO;
import com.nttdata.accounts.domain.dto.MovementDTO;
import com.nttdata.accounts.domain.entity.Movement;
import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.repository.AccountRepository;
import com.nttdata.accounts.repository.MovementRepository;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.service.MovementService;
import com.nttdata.accounts.service.mapper.AccountMapper;
import com.nttdata.accounts.service.mapper.MovementMapper;
import com.nttdata.accounts.util.ResourseApplication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final MovementMapper movementMapper;

    private final AccountMapper accountMapper;

    @Override
    public List<MovementDTO> getAllMovements() {
        List<MovementDTO> movements = movementRepository.findAll().stream().map(movementMapper::toMovementDTO).toList();
        if (movements.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("movements.not.found"));
        }
        return movements;
    }

    @Override
    public MovementDTO getMovementById(Long id) {
        MovementDTO movement = movementRepository.findById(id).map(movementMapper::toMovementDTO).orElseThrow(() -> {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("movement.not.found"));
        });
        return movement;
    }

    @Override
    public MovementDTO saveMovement(MovementDTO movement) {
        AccountDTO account = accountService.getAccountByNumber(movement.getCuenta().getNumeroCuenta());
        validateBalance(account.getSaldoInicial(), movement.getValor());
        Double newBalance = calculateBalance(account.getSaldoInicial(), movement.getValor());

        movement.setFecha(new Date());
        movement.setDescripcion(createDescription(movement));
        movement.setTipoMovimiento(account.getTipoCuenta());
        movement.setSaldoInicial(account.getSaldoInicial());
        movement.setSaldoFinal(newBalance);

        account.setSaldoInicial(newBalance);
        accountRepository.save(accountMapper.toAccount(account));

        Movement movementToSave = movementMapper.toMovement(movement);
        movementToSave.setCuenta(accountMapper.toAccount(account));

        return movementMapper.toMovementDTO(movementRepository.save(movementToSave));
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

    public String createDescription(MovementDTO movement) {
        return movement.getValor() > 0
                ? ResourseApplication.properties.getProperty("balance.positive") + " " + String.valueOf(movement.getValor())
                : ResourseApplication.properties.getProperty("balance.negative") + " " + String.valueOf(movement.getValor());
    }

}
