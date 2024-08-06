package com.nttdata.clients.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.clients.domain.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    public List<Client> findByEstado(Integer status);

    public Client findByIdAndEstado(Long id, Integer status);

    public Client findByNombreAndEstado(String name, Integer status);

}
