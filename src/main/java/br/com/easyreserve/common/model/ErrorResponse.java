package br.com.easyreserve.common.model;

import java.time.LocalDateTime;

public record ErrorResponse(int httpCode, String message, LocalDateTime timestamp) {
}