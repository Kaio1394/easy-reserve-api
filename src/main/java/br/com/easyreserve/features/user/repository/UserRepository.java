package br.com.easyreserve.features.user.repository;

import br.com.easyreserve.features.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.workspace")
    List<UserEntity> findAllWithWorkspace();

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.workspace WHERE u.workspace.id = :workspaceId AND u.active = true")
    List<UserEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.workspace WHERE u.id = :id")
    Optional<UserEntity> findByIdWithWorkspace(@Param("id") UUID id);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.workspace WHERE u.username = :username")
    Optional<UserEntity> findUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.workspace WHERE u.email = :email")
    Optional<UserEntity> findUserByEmail(@Param("email") String email);
}
