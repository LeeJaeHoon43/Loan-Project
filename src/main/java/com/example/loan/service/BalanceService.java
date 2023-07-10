package com.example.loan.service;

import com.example.loan.dto.BalanceDTO;

public interface BalanceService {
    BalanceDTO.Response create(Long applicationId, BalanceDTO.CreateRequest request);
    BalanceDTO.Response get(Long applicationId);
    BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request);
    BalanceDTO.Response repaymentUpdate(Long applicationId, BalanceDTO.RepaymentRequest request);
    void delete(Long applicationId);
}
