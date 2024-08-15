package com.nttdata.accounts.service;

import java.util.List;

import com.nttdata.accounts.domain.dto.MovementDTO;

public interface MovementService {

    List<MovementDTO> getAllMovements();

    MovementDTO getMovementById(Long id);

    MovementDTO saveMovement(MovementDTO movement);

    void validateBalance(Double accountBalance, Double amount);

    Double calculateBalance(Double accountBalance, Double amount);

}
