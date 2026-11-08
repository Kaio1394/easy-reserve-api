package br.com.easyreserve.features.user.service;

import br.com.easyreserve.exception.UserAlreadyExistWithEmailException;
import br.com.easyreserve.exception.UserAlreadyExistsException;
import br.com.easyreserve.common.security.SecurityUtils;
import br.com.easyreserve.features.user.dto.UserCreateDto;
import br.com.easyreserve.features.user.dto.UserDeleteDto;
import br.com.easyreserve.features.user.dto.UserGetDto;
import br.com.easyreserve.features.user.dto.UserProfileUpdateDto;
import br.com.easyreserve.features.user.dto.UserUpdateDto;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.user.repository.UserRepository;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import br.com.easyreserve.features.workspace.repository.WorkspaceRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           WorkspaceRepository workspaceRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserGetDto add(UserCreateDto userCreateDto) {
        if (userRepository.findUserByUsername(userCreateDto.username()).isPresent()) {
            throw new UserAlreadyExistsException(userCreateDto.username());
        }

        if (userRepository.findUserByEmail(userCreateDto.email()).isPresent()) {
            throw new UserAlreadyExistWithEmailException();
        }

        WorkspaceEntity wEntity = workspaceRepository.findById(userCreateDto.workspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace não encontrado"));

        UserEntity uEntity = userCreateDto.toEntity(wEntity);
        uEntity.setPassword(passwordEncoder.encode(userCreateDto.password()));
        uEntity = userRepository.save(uEntity);
        return UserGetDto.fromEntity(uEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        List<UserEntity> users = wsId != null
                ? userRepository.findByWorkspaceId(wsId)
                : userRepository.findAllWithWorkspace();
        return users.stream()
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .map(UserGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void delete(UserDeleteDto userDeleteDto) {
        UserEntity uEntity = userRepository.findById(userDeleteDto.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // Soft Delete: desativa o usuário preservando histórico de auditoria
        uEntity.setActive(Boolean.FALSE);
        userRepository.save(uEntity);
    }

    @Transactional
    @Override
    public UserGetDto update(UserUpdateDto userUpdateDto) {
        UserEntity uEntity = userRepository.findByIdWithWorkspace(userUpdateDto.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (userUpdateDto.name() != null && !userUpdateDto.name().isBlank()) {
            uEntity.setName(userUpdateDto.name());
        }
        if (userUpdateDto.username() != null && !userUpdateDto.username().equals(uEntity.getUsername())) {
            if (userRepository.findUserByUsername(userUpdateDto.username()).isPresent()) {
                throw new UserAlreadyExistsException(userUpdateDto.username());
            }
            uEntity.setUsername(userUpdateDto.username());
        }
        if (userUpdateDto.email() != null && !userUpdateDto.email().equals(uEntity.getEmail())) {
            if (userRepository.findUserByEmail(userUpdateDto.email()).isPresent()) {
                throw new UserAlreadyExistWithEmailException();
            }
            uEntity.setEmail(userUpdateDto.email());
        }
        if (userUpdateDto.password() != null && !userUpdateDto.password().isBlank()) {
            uEntity.setPassword(passwordEncoder.encode(userUpdateDto.password()));
        }
        if (userUpdateDto.phone() != null) {
            uEntity.setPhone(userUpdateDto.phone());
        }
        if (userUpdateDto.cpf() != null) {
            uEntity.setCpf(userUpdateDto.cpf());
        }
        if (userUpdateDto.role() != null) {
            uEntity.setRole(userUpdateDto.role());
        }
        if (userUpdateDto.active() != null) {
            uEntity.setActive(userUpdateDto.active());
        }

        uEntity = userRepository.save(uEntity);
        return UserGetDto.fromEntity(uEntity);
    }

    @Transactional
    @Override
    public UserGetDto updateProfile(UserProfileUpdateDto dto) {
        UserEntity current = SecurityUtils.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Usuário não autenticado"));

        UserEntity uEntity = userRepository.findByIdWithWorkspace(current.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) {
            uEntity.setName(dto.name().trim());
        }
        if (dto.username() != null && !dto.username().isBlank() && !dto.username().equals(uEntity.getUsername())) {
            if (userRepository.findUserByUsername(dto.username().trim()).isPresent()) {
                throw new UserAlreadyExistsException(dto.username().trim());
            }
            uEntity.setUsername(dto.username().trim());
        }
        if (dto.email() != null && !dto.email().isBlank() && !dto.email().equalsIgnoreCase(uEntity.getEmail())) {
            if (userRepository.findUserByEmail(dto.email().trim()).isPresent()) {
                throw new UserAlreadyExistWithEmailException();
            }
            uEntity.setEmail(dto.email().trim());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            uEntity.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.phone() != null) {
            uEntity.setPhone(dto.phone().trim());
        }
        if (dto.cpf() != null) {
            uEntity.setCpf(dto.cpf().trim());
        }

        uEntity = userRepository.save(uEntity);
        return UserGetDto.fromEntity(uEntity);
    }
}
