package com.nttdata.accounts.service;

import java.util.List;
import java.util.Optional;

import com.nttdata.accounts.domain.entity.Movement;

public interface MovementService {

    List<Movement> getAllMovements();

    Optional<Movement> getMovementById(Long id);

    Movement saveMovement(Movement movement);

    void validateBalance (Double accountBalance, Double amount);

    Double calculateBalance (Double accountBalance, Double amount);

}
