package com.rank.casino.service.mapper;

import com.rank.casino.domain.Transactions;
import com.rank.casino.service.dto.TransactionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transactions} and its DTO {@link TransactionsDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlayerMapper.class })
public interface TransactionsMapper extends EntityMapper<TransactionsDTO, Transactions> {
    @Mapping(target = "player", source = "player", qualifiedByName = "id")
    TransactionsDTO toDto(Transactions s);
}
