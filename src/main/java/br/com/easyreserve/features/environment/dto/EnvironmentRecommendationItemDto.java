package br.com.easyreserve.features.environment.dto;

import java.util.List;
import java.util.UUID;

public record EnvironmentRecommendationItemDto(
        UUID environmentId,
        String environmentName,
        String description,
        Integer capacity,
        double matchScore,
        int matchPercentage,
        List<String> matchedResources,
        List<String> missingResources,
        boolean available,
        String keyStatus,
        String reason
) {
}
