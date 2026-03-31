package com.orion.vendorvault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
    private int status;
    private String error;
    private String errorCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<FieldErrorDto> fieldErrors;
}

