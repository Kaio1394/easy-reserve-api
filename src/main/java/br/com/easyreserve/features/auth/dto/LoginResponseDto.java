package br.com.easyreserve.features.auth.dto;

import br.com.easyreserve.features.user.dto.UserGetDto;
import br.com.easyreserve.features.workspace.dto.WorkspaceGetDto;

public record LoginResponseDto(
        WorkspaceGetDto workspace,
        UserGetDto user,
        String token,
        long expiresIn
) {
}
