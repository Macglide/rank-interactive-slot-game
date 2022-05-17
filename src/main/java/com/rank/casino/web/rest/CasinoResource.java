package com.rank.casino.web.rest;

import com.rank.casino.service.CasinoService;
import com.rank.casino.web.api.PlayerApiDelegate;
import com.rank.casino.web.api.model.BalanceResponseDTO;
import com.rank.casino.web.api.model.UpdateBalanceRequestDTO;
import com.rank.casino.web.api.model.UpdateBalanceResponseDTO;
import com.rank.casino.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
public class CasinoResource implements PlayerApiDelegate {

    @Autowired
    private CasinoService casinoService;

    @Override
    public ResponseEntity<BalanceResponseDTO> getBalance(Long playerId) {

        return ResponseEntity.ok(casinoService.getPlayerBalance(playerId));

    }

    @Override
    public ResponseEntity<UpdateBalanceResponseDTO> updateBalance(Long playerId,
                                                                  UpdateBalanceRequestDTO updateBalanceRequestDTO) {

        return ResponseEntity.ok(casinoService.updatePlayerBalance(playerId, updateBalanceRequestDTO.getAmount(),
            updateBalanceRequestDTO.getTransactionType().getValue()));
    }

}
