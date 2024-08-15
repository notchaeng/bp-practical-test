package com.nttdata.accounts.domain.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MovementDTO {

    private Long id;
    private Date fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldoInicial;
    private double saldoFinal;
    private String descripcion;
    private AccountDTO cuenta;

}
