package com.rank.casino.repository;

import com.rank.casino.domain.Player;
import com.rank.casino.domain.Transactions;
import com.rank.casino.service.dto.PlayerDTO;
import com.rank.casino.service.dto.TransactionsDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Transactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    Optional<Transactions> findFirstByPlayerIdOrderByIdDesc(long playId);

    List<Transactions> findFirst10ByPlayerIdOrderByIdDesc(long playerId);
}
