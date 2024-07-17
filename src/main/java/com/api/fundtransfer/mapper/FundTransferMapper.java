package com.api.fundtransfer.mapper;

import com.api.fundtransfer.dto.FundTransferRequest;
import com.api.fundtransfer.dto.FundTransferResponse;
import com.api.fundtransfer.entity.FundTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FundTransferMapper {
    FundTransferMapper INSTANCE = Mappers.getMapper(FundTransferMapper.class);

    FundTransferResponse entityToDTO(FundTransfer fundTransfer);

    FundTransfer dTOToEntity(FundTransferRequest fundTransferRequest);
}
