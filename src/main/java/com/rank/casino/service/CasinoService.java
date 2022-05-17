package com.rank.casino.service;

import com.rank.casino.config.Constants;
import com.rank.casino.service.dto.PlayerDTO;
import com.rank.casino.service.dto.TransactionsDTO;
import com.rank.casino.web.api.model.*;
import com.rank.casino.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CasinoService {

    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private PlayerService playerService;

    public BalanceResponseDTO getPlayerBalance(Long playerId) {

        //verify if the player exist
        Optional<PlayerDTO> optionalPlayerDTO = playerService.findOne(playerId);
        if(!optionalPlayerDTO.isPresent()){
            throw new BadRequestAlertException("Invalid PlayerId: " + playerId, "Balance", "");
        }
        PlayerDTO playerDTO = optionalPlayerDTO.get();

        //get balance
        Optional<TransactionsDTO> optionalTransactions = transactionsService.findFirstByPlayerIdOrderByIdDesc(playerDTO.getId());
        if(!optionalTransactions.isPresent())
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Player not configured");
        }
        TransactionsDTO transactions = optionalTransactions.get();

        return new BalanceResponseDTO()
            .balance(transactions.getBalance())
            .playerId(transactions.getPlayer().getId());
    }

    public UpdateBalanceResponseDTO updatePlayerBalance(Long playerId, BigDecimal amount, String transactionType) {

        //verify if the player exist
        Optional<PlayerDTO> optionalPlayerDTO = playerService.findOne(playerId);
        if(!optionalPlayerDTO.isPresent()){
            throw new BadRequestAlertException("Invalid PlayerId: " + playerId, "Balance", "");
        }
        PlayerDTO playerDTO = optionalPlayerDTO.get();

        Optional<TransactionsDTO> optionalTransactions = transactionsService.findFirstByPlayerIdOrderByIdDesc(playerDTO.getId());
        if(!optionalTransactions.isPresent())
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Player not configured");
        }

        //balance == balance - amount. if amount > balance, teapot(Http 418). This is for WAGER
        TransactionsDTO transactions = optionalTransactions.get();
        BigDecimal balance = new BigDecimal(0);

        //If transactionType is WAGER
        if(transactionType.equalsIgnoreCase(Constants.WAGER_TRANSACTION_TYPE))
        {
            if(amount.compareTo(transactions.getBalance()) > 0)
                throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Insufficient balance");

            balance = transactions.getBalance().add(amount.negate());
        }
        //else if it's a WIN
        else
            balance = transactions.getBalance().add(amount);

        //log the transaction
        TransactionsDTO transactionsDTO = new TransactionsDTO();
        transactionsDTO.setBalance(balance);
        transactionsDTO.setPlayer(playerDTO);
        transactionsDTO.setAmount(amount);
        transactionsDTO.setTransactionId(UUID.randomUUID().toString());
        transactionsDTO.setTransactionType(transactionType);
        transactionsDTO.setTransactionDate(ZonedDateTime.now());

        transactionsService.save(transactionsDTO);

        return new UpdateBalanceResponseDTO()
            .balance(transactionsDTO.getBalance())
            .transactionId(transactionsDTO.getTransactionId());

    }

    public TransactionHistoryResponseDTO getPlayerTransactionHistory(String username) {

        TransactionHistoryResponseDTO historyResponseDTO = new TransactionHistoryResponseDTO();

        //get playerId
        Optional<PlayerDTO> playerDTOOptional = playerService.findByUsername(username);
        if(!playerDTOOptional.isPresent())
            throw new BadRequestAlertException("Invalid username: " + username, "TransactionHistory", "");

        PlayerDTO playerDTO = playerDTOOptional.get();
        //get last 10 transactions
        List<TransactionsDTO> transactionsDTOList = transactionsService.findFirst10ByPlayerIdOrderByIdDesc(playerDTO.getId());

        if(transactionsDTOList.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Player not configured");
        }

        //build response body
        for (TransactionsDTO transactionsDTO: transactionsDTOList) {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionId(transactionsDTO.getTransactionId());
            transactionDTO.setTransactionType(TransactionDTO.TransactionTypeEnum.valueOf(transactionsDTO.getTransactionType()));
            transactionDTO.setAmount(transactionsDTO.getAmount());
            historyResponseDTO.addTransactionsItem(transactionDTO);
        }

        return historyResponseDTO;
    }
}
