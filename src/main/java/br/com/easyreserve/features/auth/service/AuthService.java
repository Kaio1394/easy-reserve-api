package br.com.easyreserve.features.auth.service;

import br.com.easyreserve.features.auth.dto.LoginRequestDto;
import br.com.easyreserve.features.auth.dto.LoginResponseDto;
import br.com.easyreserve.features.auth.dto.RegisterRequestDto;
import br.com.easyreserve.features.auth.dto.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto request);
    LoginResponseDto login(LoginRequestDto request);
}
