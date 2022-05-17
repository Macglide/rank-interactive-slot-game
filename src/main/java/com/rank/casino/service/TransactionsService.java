package com.rank.casino.service;

import com.rank.casino.service.dto.TransactionsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.rank.casino.domain.Transactions}.
 */
public interface TransactionsService {
    /**
     * Save a transactions.
     *
     * @param transactionsDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionsDTO save(TransactionsDTO transactionsDTO);

    /**
     * Partially updates a transactions.
     *
     * @param transactionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionsDTO> partialUpdate(TransactionsDTO transactionsDTO);

    /**
     * Get all the transactions.
     *
     * @return the list of entities.
     */
    List<TransactionsDTO> findAll();

    /**
     * Get the "id" transactions.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionsDTO> findOne(Long id);

    /**
     * Delete the "id" transactions.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<TransactionsDTO> findFirstByPlayerIdOrderByIdDesc(long playerId);

    List<TransactionsDTO> findFirst10ByPlayerIdOrderByIdDesc(long playerId);
}
