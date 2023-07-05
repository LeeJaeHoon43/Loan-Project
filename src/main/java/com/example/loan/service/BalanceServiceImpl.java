package com.example.loan.service;

import com.example.loan.domain.Balance;
import com.example.loan.dto.BalanceDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{

    private final BalanceRepository balanceRepository;
    private final ModelMapper modelMapper;

    @Override
    public BalanceDTO.Response create(Long applicationId, BalanceDTO.CreateRequest request) {
        Balance balance = modelMapper.map(request, Balance.class);

        // 첫 생성은 entry amount를 balance.
        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findAllByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(b.getUpdatedAt());
        });

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, BalanceDTO.Response.class);
    }

    @Override
    public BalanceDTO.Response get(Long applicationId) {
        Balance balance = balanceRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(balance, BalanceDTO.Response.class);
    }

    @Override
    public BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request) {
        Balance balance = balanceRepository.findAllByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();
        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);

        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, BalanceDTO.Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Balance balance = balanceRepository.findAllByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        balance.setIsDeleted(true);

        balanceRepository.save(balance);
    }
}
