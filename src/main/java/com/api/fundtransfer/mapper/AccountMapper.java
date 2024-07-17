package com.api.fundtransfer.mapper;

import com.api.fundtransfer.dto.AccountRequest;
import com.api.fundtransfer.dto.AccountResponse;
import com.api.fundtransfer.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountResponse entityToDTO(Account account);

    Account dTOToEntity(AccountRequest accountRequest);
}
