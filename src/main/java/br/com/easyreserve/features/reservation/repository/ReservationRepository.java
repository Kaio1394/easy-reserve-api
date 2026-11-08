package br.com.easyreserve.features.reservation.repository;

import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.workspace JOIN FETCH r.environment JOIN FETCH r.key JOIN FETCH r.user ORDER BY r.createdAt DESC")
    List<ReservationEntity> findAllWithDetails();

    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.workspace JOIN FETCH r.environment JOIN FETCH r.key JOIN FETCH r.user WHERE r.workspace.id = :workspaceId AND r.active = true ORDER BY r.createdAt DESC")
    List<ReservationEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.workspace JOIN FETCH r.environment JOIN FETCH r.key JOIN FETCH r.user WHERE r.id = :id")
    Optional<ReservationEntity> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.workspace JOIN FETCH r.environment JOIN FETCH r.key JOIN FETCH r.user WHERE r.environment.id = :environmentId AND r.status = :status AND r.active = true")
    List<ReservationEntity> findActiveByEnvironmentId(@Param("environmentId") UUID environmentId, @Param("status") ReservationStatus status);
}
