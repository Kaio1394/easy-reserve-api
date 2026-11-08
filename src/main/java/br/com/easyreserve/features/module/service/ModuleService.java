package br.com.easyreserve.features.module.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.module.dto.ModuleCreateDto;
import br.com.easyreserve.features.module.dto.ModuleDeleteDto;
import br.com.easyreserve.features.module.dto.ModuleGetDto;
import br.com.easyreserve.features.module.dto.ModuleUpdateDto;

public interface ModuleService extends BaseService<ModuleCreateDto, ModuleUpdateDto, ModuleGetDto, ModuleDeleteDto> {
}
