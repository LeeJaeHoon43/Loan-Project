package com.example.loan.service;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.CounselRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CounselServiceTest {

    @InjectMocks
    public CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() {
        Counsel entity = Counsel.builder()
                .name("Member Lee")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .memo("I hope to get a loan")
                .zipCode("123456")
                .address("Somewhere in Gangnam-gu, Seoul")
                .addressDetail("What Apartment No. 101, 1st floor No. 101")
                .build();

        CounselDTO.Request request = CounselDTO.Request.builder()
                .name("Member Lee")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .memo("I hope to get a loan")
                .zipCode("123456")
                .address("Somewhere in Gangnam-gu, Seoul")
                .addressDetail("What Apartment No. 101, 1st floor No. 101")
                .build();

        when(counselRepository.save(any(Counsel.class))).thenReturn(entity);
        CounselDTO.Response actual = counselService.create(request);
        assertThat(actual.getName()).isSameAs(entity.getName());
    }

    @Test
    void Should_ReturnResponseOfExistCounselEntity_When_RequestExistCounselId(){
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        CounselDTO.Response actual = counselService.get(1L);
        assertThat(actual.getCounselId()).isSameAs(findId);
    }

    @Test
    void Should_ThrowException_When_RequestNotExistCounselId(){
        Long findId = 2L;

        when(counselRepository.findById(findId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        assertThrows(BaseException.class, () -> counselService.get(2L));
    }
}