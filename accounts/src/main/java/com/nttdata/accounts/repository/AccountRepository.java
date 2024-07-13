package com.nttdata.accounts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.accounts.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public List<Account> findByEstado(Integer status);

    public Account findByIdAndEstado(Long id, Integer status);

    public Account findByNumeroCuentaAndEstado(String accountNumber, Integer status);

}
