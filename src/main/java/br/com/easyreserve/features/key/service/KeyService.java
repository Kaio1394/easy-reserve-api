package br.com.easyreserve.features.key.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.key.dto.KeyCreateDto;
import br.com.easyreserve.features.key.dto.KeyDeleteDto;
import br.com.easyreserve.features.key.dto.KeyGetDto;
import br.com.easyreserve.features.key.dto.KeyUpdateDto;

public interface KeyService extends BaseService<KeyCreateDto, KeyUpdateDto, KeyGetDto, KeyDeleteDto> {
}
