package com.nttdata.clients.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.clients.domain.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    public List<Client> findByEstado(Integer status);

    public Optional<Client> findByIdentificacionAndEstado(String identification, Integer status);

}
