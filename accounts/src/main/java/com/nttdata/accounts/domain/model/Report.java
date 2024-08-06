package com.nttdata.accounts.domain.model;

public interface Report {

    String getFecha();

    String getNombre();

    String getNumero_cuenta();

    String getTipo_movimiento();

    Double getSaldo_inicial();

    Double getValor();

    Double getSaldo_final();
}
