package br.com.easyreserve.features.environment.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.environment.dto.*;

import java.util.List;

public interface EnvironmentService extends BaseService<EnvironmentCreateDto, EnvironmentUpdateDto, EnvironmentGetDto, EnvironmentDeleteDto> {
    List<EnvironmentRecommendationItemDto> recommend(EnvironmentRecommendationRequestDto dto);
}
