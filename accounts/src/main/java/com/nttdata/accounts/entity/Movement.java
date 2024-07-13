package com.nttdata.accounts.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "MOVIMIENTOS")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldoInicial;
    private double saldoFinal;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Account cuenta;
}
