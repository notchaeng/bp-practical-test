package com.nttdata.accounts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.accounts.domain.dto.MovementDTO;
import com.nttdata.accounts.service.MovementService;

@RestController
@RequestMapping("/movimientos")
public class MovementController {

    public static Logger logger = LogManager.getLogger();

    @Autowired
    private MovementService movementService;

    @GetMapping
    public ResponseEntity<List<MovementDTO>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getMovementById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(movementService.getMovementById(id));
    }

    @PostMapping
    public ResponseEntity<MovementDTO> createMovement(@RequestBody MovementDTO movement) {
        return ResponseEntity.ok(movementService.saveMovement(movement));

    }
}
