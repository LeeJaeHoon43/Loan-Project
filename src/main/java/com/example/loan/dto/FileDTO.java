package com.example.loan.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO implements Serializable {
    private String name;
    private String url;
}
