package br.com.easyreserve.features.reservation_history.service;

import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation_history.dto.ReservationHistoryGetDto;
import br.com.easyreserve.features.reservation_history.enums.ReservationHistoryAction;

import java.util.List;
import java.util.UUID;

public interface ReservationHistoryService {
    void recordHistory(ReservationEntity reservation, ReservationHistoryAction action, String notes, boolean detachReservationRef);
    List<ReservationHistoryGetDto> findAll();
    List<ReservationHistoryGetDto> findByEnvironmentId(UUID environmentId);
}
