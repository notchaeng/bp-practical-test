package com.nttdata.accounts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.accounts.domain.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public List<Account> findByEstado(Integer status);

    public Optional<Account> findByNumeroCuentaAndEstado(String accountNumber, Integer status);

}
