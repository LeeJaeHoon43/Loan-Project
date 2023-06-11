package com.example.loan.service;

import com.example.loan.dto.ApplicationDTO;

public interface ApplicationService {

    ApplicationDTO.Response create(ApplicationDTO.Request request);
}
