package br.com.easyreserve.features.user.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.user.dto.UserCreateDto;
import br.com.easyreserve.features.user.dto.UserDeleteDto;
import br.com.easyreserve.features.user.dto.UserGetDto;
import br.com.easyreserve.features.user.dto.UserProfileUpdateDto;
import br.com.easyreserve.features.user.dto.UserUpdateDto;

public interface UserService extends BaseService<UserCreateDto, UserUpdateDto, UserGetDto, UserDeleteDto> {
    UserGetDto updateProfile(UserProfileUpdateDto dto);
}
