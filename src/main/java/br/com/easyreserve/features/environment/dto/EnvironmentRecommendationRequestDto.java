package br.com.easyreserve.features.environment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record EnvironmentRecommendationRequestDto(
        @NotNull(message = "O ID do workspace é obrigatório")
        UUID workspaceId,

        List<String> requiredResources,

        Map<String, Double> resourceWeights,

        Integer minCapacity,

        LocalDateTime startTime,

        LocalDateTime endTime
) {
}
