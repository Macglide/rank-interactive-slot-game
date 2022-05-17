package com.rank.casino.web.rest;

import com.rank.casino.service.CasinoService;
import com.rank.casino.web.api.AdminApiDelegate;
import com.rank.casino.web.api.model.TransactionHistoryRequestDTO;
import com.rank.casino.web.api.model.TransactionHistoryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionHistoryResource implements AdminApiDelegate {

    @Autowired
    private CasinoService casinoService;

    @Override
    public ResponseEntity<TransactionHistoryResponseDTO> retrieveTransactionHistory(TransactionHistoryRequestDTO transactionHistoryRequestDTO) {

        return ResponseEntity.ok(casinoService.getPlayerTransactionHistory(transactionHistoryRequestDTO.getUsername()));
    }

}
