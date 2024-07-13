package com.nttdata.accounts.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.accounts.entity.Movement;
import com.nttdata.accounts.model.Response;
import com.nttdata.accounts.service.MovementService;
import com.nttdata.accounts.util.ResourseApplication;

@RestController
@RequestMapping("/movimientos")
public class MovementController {

    public static Logger logger = LogManager.getLogger();

    @Autowired
    private MovementService movementService;

    @GetMapping
    public ResponseEntity<Response> getAllMovements() {
        Response response = new Response();
        List<Movement> movements;
        try {
            movements = movementService.getAllMovements();
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), movements);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getAllMovements method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getMovementById(@PathVariable(value = "id") Long id) {
        Response response = new Response();
        Optional<Movement> movement;
        try {
            movement = movementService.getMovementById(id);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), movement);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getMovementById method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Response> createMovement(@RequestBody Movement movement) {
        Response response = new Response();
        Movement movementSave;
        try {
            movementSave = movementService.saveMovement(movement);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.movement.update"), movementSave);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing createMovement method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
