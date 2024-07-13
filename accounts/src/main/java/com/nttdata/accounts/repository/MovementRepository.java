package com.nttdata.accounts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nttdata.accounts.entity.Movement;
import com.nttdata.accounts.model.Report;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query(nativeQuery = true, value = """
        select
            m.fecha,
            u.nombre,
            c.numero_cuenta,
            m.tipo_movimiento,
            m.saldo_inicial,
            m.valor,
            m.saldo_final
        from movimientos m
        left join cuentas c on m.cuenta_id = c.id
        left join usuarios u on c.client_id = u.id
        where
            date(fecha) between date(:startDate) and date(:endDate)
    """)
    List<Report> findReport(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
