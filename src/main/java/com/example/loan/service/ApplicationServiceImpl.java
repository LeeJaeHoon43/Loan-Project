package com.example.loan.service;

import com.example.loan.domain.AcceptTerms;
import com.example.loan.domain.Application;
import com.example.loan.domain.Terms;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.AcceptTermsRepository;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import com.example.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{

    private final ApplicationRepository applicationRepository;
    private final TermsRepository termsRepository;
    private final AcceptTermsRepository acceptTermsRepository;
    private final JudgmentRepository judgmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApplicationDTO.Response create(ApplicationDTO.Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);
        return modelMapper.map(applied, ApplicationDTO.Response.class);
    }

    @Override
    public ApplicationDTO.Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
           throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        return modelMapper.map(application, ApplicationDTO.Response.class);
    }

    @Override
    public ApplicationDTO.Response update(Long applicationId, ApplicationDTO.Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, ApplicationDTO.Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setIsDeleted(true);

        applicationRepository.save(application);
    }

    @Override
    public Boolean acceptTerms(Long applicationId, ApplicationDTO.AcceptTerms dto) {
        applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if (termsList.isEmpty()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> acceptTermsIds = dto.getAcceptTermsIds();
        if (termsList.size() != acceptTermsIds.size()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).toList();
        Collections.sort(acceptTermsIds);

        if (!termsIds.containsAll(acceptTermsIds)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for(Long termsId : acceptTermsIds){
            AcceptTerms accepted = AcceptTerms.builder()
                    .termsId(termsId)
                    .applicationId(applicationId)
                    .build();
            acceptTermsRepository.save(accepted);
        }
        return true;
    }

    @Override
    public ApplicationDTO.Response contract(Long applicationId) {
        // 신청 정보 확인
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 심사 정보 확인
        judgmentRepository.findByApplicationId(applicationId).orElseThrow(() -> {
           throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 승인 금액 > 0
        if (application.getApprovalAmount() == null || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 계약 체결
        application.setContractedAt(LocalDateTime.now());

        Application updated = applicationRepository.save(application);

        return modelMapper.map(updated, ApplicationDTO.Response.class);
    }
}
