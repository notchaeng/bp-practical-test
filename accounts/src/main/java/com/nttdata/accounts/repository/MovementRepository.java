package com.nttdata.accounts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nttdata.accounts.domain.entity.Movement;
import com.nttdata.accounts.domain.model.Report;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query(nativeQuery = true, value = """
        SELECT 
            m.fecha as fechaMovimiento,
            u.nombre as nombreCliente,
            c.numero_cuenta as numeroCuenta,
            m.tipo_movimiento as tipoMovimiento,
            m.saldo_inicial as saldoInicial,
            m.valor as valorMovimiento,
            m.saldo_final as saldoFinal
        FROM
            movimientos m
        LEFT JOIN
            cuentas c ON m.cuenta_id = c.id
        LEFT JOIN
            usuarios u ON c.client_id = u.identificacion
        WHERE
            DATE(fecha) BETWEEN DATE(:startDate) AND date(:endDate)
            AND u.identificacion = :identification
            
    """)
    List<Report> findReport(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("identification") String identification);

}
