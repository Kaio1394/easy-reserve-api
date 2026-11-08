package br.com.easyreserve.features.auth.dto;

import br.com.easyreserve.features.workspace.dto.WorkspaceGetDto;
import br.com.easyreserve.features.user.dto.UserGetDto;

public record RegisterResponseDto(
        WorkspaceGetDto workspace,
        UserGetDto user,
        String token,
        long expiresIn
) {
}
