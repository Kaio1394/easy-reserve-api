package br.com.easyreserve.features.environment_resources.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceCreateDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceDeleteDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceGetDto;
import br.com.easyreserve.features.environment_resources.dto.EnvironmentResourceUpdateDto;

public interface EnvironmentResourceService extends BaseService<EnvironmentResourceCreateDto, EnvironmentResourceUpdateDto, EnvironmentResourceGetDto, EnvironmentResourceDeleteDto> {
}
