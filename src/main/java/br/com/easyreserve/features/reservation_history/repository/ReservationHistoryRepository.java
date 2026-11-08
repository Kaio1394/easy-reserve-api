package br.com.easyreserve.features.reservation_history.repository;

import br.com.easyreserve.features.reservation_history.entity.ReservationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistoryEntity, UUID> {

    @Query("SELECT h FROM ReservationHistoryEntity h JOIN FETCH h.workspace JOIN FETCH h.environment JOIN FETCH h.key JOIN FETCH h.user LEFT JOIN FETCH h.reservation ORDER BY h.createdAt DESC")
    List<ReservationHistoryEntity> findAllWithDetails();

    @Query("SELECT h FROM ReservationHistoryEntity h JOIN FETCH h.workspace JOIN FETCH h.environment JOIN FETCH h.key JOIN FETCH h.user LEFT JOIN FETCH h.reservation WHERE h.workspace.id = :workspaceId AND h.active = true ORDER BY h.createdAt DESC")
    List<ReservationHistoryEntity> findByWorkspaceId(@Param("workspaceId") UUID workspaceId);

    @Query("SELECT h FROM ReservationHistoryEntity h JOIN FETCH h.workspace JOIN FETCH h.environment JOIN FETCH h.key JOIN FETCH h.user LEFT JOIN FETCH h.reservation WHERE h.environment.id = :environmentId ORDER BY h.createdAt DESC")
    List<ReservationHistoryEntity> findByEnvironmentIdWithDetails(@Param("environmentId") UUID environmentId);

    @Modifying
    @Query("UPDATE ReservationHistoryEntity h SET h.reservation = null WHERE h.reservation.id = :reservationId")
    void clearReservationReference(@Param("reservationId") UUID reservationId);
}
