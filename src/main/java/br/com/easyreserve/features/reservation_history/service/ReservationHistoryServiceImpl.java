package br.com.easyreserve.features.reservation_history.service;

import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation_history.dto.ReservationHistoryGetDto;
import br.com.easyreserve.features.reservation_history.entity.ReservationHistoryEntity;
import br.com.easyreserve.features.reservation_history.enums.ReservationHistoryAction;
import br.com.easyreserve.features.reservation_history.repository.ReservationHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationHistoryServiceImpl implements ReservationHistoryService {

    private final ReservationHistoryRepository reservationHistoryRepository;

    public ReservationHistoryServiceImpl(ReservationHistoryRepository reservationHistoryRepository) {
        this.reservationHistoryRepository = reservationHistoryRepository;
    }

    @Transactional
    @Override
    public void recordHistory(ReservationEntity reservation,
                              ReservationHistoryAction action,
                              String notes,
                              boolean detachReservationRef) {
        if (detachReservationRef && reservation.getId() != null) {
            reservationHistoryRepository.clearReservationReference(reservation.getId());
        }
        ReservationHistoryEntity history = new ReservationHistoryEntity();
        history.setWorkspace(reservation.getWorkspace());
        history.setReservation(detachReservationRef ? null : reservation);
        history.setEnvironment(reservation.getEnvironment());
        history.setKey(reservation.getKey());
        history.setUser(reservation.getUser());
        history.setAction(action);
        history.setPurpose(reservation.getPurpose());
        history.setStartTime(reservation.getStartTime());
        history.setEndTime(reservation.getEndTime());
        history.setStatus(reservation.getStatus());
        history.setNotes(notes);
        history.setActive(true);
        reservationHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationHistoryGetDto> findAll() {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        List<ReservationHistoryEntity> histories = wsId != null
                ? reservationHistoryRepository.findByWorkspaceId(wsId)
                : reservationHistoryRepository.findAllWithDetails();
        return histories.stream()
                .filter(h -> Boolean.TRUE.equals(h.getActive()))
                .map(ReservationHistoryGetDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationHistoryGetDto> findByEnvironmentId(UUID environmentId) {
        java.util.UUID wsId = br.com.easyreserve.common.security.SecurityUtils.getCurrentWorkspaceId();
        return reservationHistoryRepository.findByEnvironmentIdWithDetails(environmentId)
                .stream()
                .filter(h -> Boolean.TRUE.equals(h.getActive()))
                .filter(h -> wsId == null || (h.getWorkspace() != null && h.getWorkspace().getId().equals(wsId)))
                .map(ReservationHistoryGetDto::fromEntity)
                .toList();
    }
}
