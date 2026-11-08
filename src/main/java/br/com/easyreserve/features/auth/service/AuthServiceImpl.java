package br.com.easyreserve.features.auth.service;

import br.com.easyreserve.common.security.JwtService;
import br.com.easyreserve.exception.UserAlreadyExistWithEmailException;
import br.com.easyreserve.exception.UserAlreadyExistsException;
import br.com.easyreserve.exception.WorkspaceAlreadyExistsException;
import br.com.easyreserve.features.auth.dto.LoginRequestDto;
import br.com.easyreserve.features.auth.dto.LoginResponseDto;
import br.com.easyreserve.features.auth.dto.RegisterRequestDto;
import br.com.easyreserve.features.auth.dto.RegisterResponseDto;
import br.com.easyreserve.features.module.entity.ModuleEntity;
import br.com.easyreserve.features.module.repository.ModuleRepository;
import br.com.easyreserve.features.user.dto.UserGetDto;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.user.enums.Role;
import br.com.easyreserve.features.user.repository.UserRepository;
import br.com.easyreserve.features.workspace.dto.WorkspaceGetDto;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import br.com.easyreserve.features.workspace_module.entity.WorkspaceModuleEntity;
import br.com.easyreserve.features.workspace_module.repository.WorkspaceModuleRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final WorkspaceModuleRepository workspaceModuleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(WorkspaceRepository workspaceRepository,
                           UserRepository userRepository,
                           ModuleRepository moduleRepository,
                           WorkspaceModuleRepository workspaceModuleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.workspaceModuleRepository = workspaceModuleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {
        if (workspaceRepository.findWorkspaceBySlug(request.workspaceSlug()).isPresent()) {
            throw new WorkspaceAlreadyExistsException(request.workspaceSlug());
        }

        if (userRepository.findUserByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException(request.username());
        }

        if (userRepository.findUserByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistWithEmailException();
        }

        WorkspaceEntity workspace = new WorkspaceEntity();
        workspace.setName(request.workspaceName());
        workspace.setSlug(request.workspaceSlug());
        workspace = workspaceRepository.save(workspace);

        List<ModuleEntity> modulesToLink;
        if (request.moduleIds() != null && !request.moduleIds().isEmpty()) {
            modulesToLink = moduleRepository.findAllById(request.moduleIds())
                    .stream()
                    .filter(m -> Boolean.TRUE.equals(m.getActive()))
                    .toList();
        } else {
            modulesToLink = moduleRepository.findAll()
                    .stream()
                    .filter(m -> Boolean.TRUE.equals(m.getActive()))
                    .toList();
        }

        for (ModuleEntity module : modulesToLink) {
            WorkspaceModuleEntity wm = new WorkspaceModuleEntity();
            wm.setWorkspace(workspace);
            wm.setModule(module);
            wm.setActive(true);
            workspaceModuleRepository.save(wm);
        }

        UserEntity user = new UserEntity();
        user.setWorkspace(workspace);
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setCpf(request.cpf());
        user.setRole(Role.OWNER);
        user = userRepository.save(user);

        String token = jwtService.generate(user);

        return new RegisterResponseDto(
                WorkspaceGetDto.fromEntity(workspace),
                UserGetDto.fromEntity(user),
                token,
                jwtService.getExpiration()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findUserByUsername(request.login())
                .or(() -> userRepository.findUserByEmail(request.login()))
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!Boolean.TRUE.equals(user.getActive()) || !Boolean.TRUE.equals(user.getWorkspace().getActive())) {
            throw new BadCredentialsException("Usuário ou workspace inativo");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generate(user);

        return new LoginResponseDto(
                WorkspaceGetDto.fromEntity(user.getWorkspace()),
                UserGetDto.fromEntity(user),
                token,
                jwtService.getExpiration()
        );
    }
}
