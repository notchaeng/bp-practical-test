package com.nttdata.accounts.service.mapper;

import org.mapstruct.Mapper;

import com.nttdata.accounts.domain.dto.AccountDTO;
import com.nttdata.accounts.domain.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(AccountDTO accountDTO);

    AccountDTO toAccountDTO(Account account);
}
