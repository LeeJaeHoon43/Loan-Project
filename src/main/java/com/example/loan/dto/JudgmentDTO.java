package com.example.loan.dto;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JudgmentDTO implements Serializable {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long applicationId;
        private String name;
        private BigDecimal approvalAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long judgmentId;
        private Long applicationId;
        private String name;
        private BigDecimal approvalAmount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
