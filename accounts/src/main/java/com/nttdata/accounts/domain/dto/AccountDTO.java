package com.nttdata.accounts.domain.dto;

import lombok.Data;

@Data
public class AccountDTO {

    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private Integer estado;
    private String clienteId;

}
