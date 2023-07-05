package com.example.loan.service;

import com.example.loan.domain.Balance;
import com.example.loan.dto.BalanceDTO;
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

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, BalanceDTO.Response.class);
    }

    @Override
    public BalanceDTO.Response get(Long applicationId) {
        return null;
    }

    @Override
    public BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request) {
        return null;
    }

    @Override
    public void delete(Long applicationId) {

    }
}
