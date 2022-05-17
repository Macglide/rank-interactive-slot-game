package com.rank.casino.service.impl;

import com.rank.casino.domain.Transactions;
import com.rank.casino.repository.TransactionsRepository;
import com.rank.casino.service.TransactionsService;
import com.rank.casino.service.dto.PlayerDTO;
import com.rank.casino.service.dto.TransactionsDTO;
import com.rank.casino.service.mapper.TransactionsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transactions}.
 */
@Service
@Transactional
public class TransactionsServiceImpl implements TransactionsService {

    private final Logger log = LoggerFactory.getLogger(TransactionsServiceImpl.class);

    private final TransactionsRepository transactionsRepository;

    private final TransactionsMapper transactionsMapper;

    public TransactionsServiceImpl(TransactionsRepository transactionsRepository, TransactionsMapper transactionsMapper) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
    }

    @Override
    public TransactionsDTO save(TransactionsDTO transactionsDTO) {
        log.debug("Request to save Transactions : {}", transactionsDTO);
        Transactions transactions = transactionsMapper.toEntity(transactionsDTO);
        transactions = transactionsRepository.save(transactions);
        return transactionsMapper.toDto(transactions);
    }

    @Override
    public Optional<TransactionsDTO> partialUpdate(TransactionsDTO transactionsDTO) {
        log.debug("Request to partially update Transactions : {}", transactionsDTO);

        return transactionsRepository
            .findById(transactionsDTO.getId())
            .map(existingTransactions -> {
                transactionsMapper.partialUpdate(existingTransactions, transactionsDTO);

                return existingTransactions;
            })
            .map(transactionsRepository::save)
            .map(transactionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionsDTO> findAll() {
        log.debug("Request to get all Transactions");
        return transactionsRepository.findAll().stream().map(transactionsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionsDTO> findOne(Long id) {
        log.debug("Request to get Transactions : {}", id);
        return transactionsRepository.findById(id).map(transactionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transactions : {}", id);
        transactionsRepository.deleteById(id);
    }

    @Override
    public Optional<TransactionsDTO> findFirstByPlayerIdOrderByIdDesc(long playerId) {
        log.debug("Request to get Player's balance : {}", playerId);

        return transactionsRepository.findFirstByPlayerIdOrderByIdDesc(playerId).map(transactionsMapper::toDto);
    }

    @Override
    public List<TransactionsDTO> findFirst10ByPlayerIdOrderByIdDesc(long playerId) {
        log.debug("Request to get last 10 transactions for playerId : {}", playerId);
        return transactionsRepository.findFirst10ByPlayerIdOrderByIdDesc(playerId).stream().map(transactionsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
