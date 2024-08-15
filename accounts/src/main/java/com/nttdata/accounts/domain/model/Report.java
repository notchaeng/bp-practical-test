package com.nttdata.accounts.domain.model;

public interface Report {

    String getFechaMovimiento();

    String getNombreCliente();

    String geNumeroCuenta();

    String getTipoMovimiento();

    Double getSaldoInicial();

    Double getValorMovimiento();

    Double getSaldoFinal();
}
