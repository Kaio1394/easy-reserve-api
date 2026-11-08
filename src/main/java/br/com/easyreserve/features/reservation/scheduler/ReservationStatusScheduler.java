package br.com.easyreserve.features.reservation.scheduler;

import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.key.repository.KeyRepository;
import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.reservation.repository.ReservationRepository;
import br.com.easyreserve.features.reservation_history.enums.ReservationHistoryAction;
import br.com.easyreserve.features.reservation_history.service.ReservationHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationStatusScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReservationStatusScheduler.class);

    private final ReservationRepository reservationRepository;
    private final KeyRepository keyRepository;
    private final ReservationHistoryService reservationHistoryService;

    public ReservationStatusScheduler(ReservationRepository reservationRepository,
                                     KeyRepository keyRepository,
                                     ReservationHistoryService reservationHistoryService) {
        this.reservationRepository = reservationRepository;
        this.keyRepository = keyRepository;
        this.reservationHistoryService = reservationHistoryService;
    }

    @Transactional
    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void checkAndSyncReservationsAndKeys() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Busca todas as reservas ativas
        List<ReservationEntity> allReservations = reservationRepository.findAllWithDetails();

        // 2. Conclui automaticamente as reservas cujo horário limite já expirou
        for (ReservationEntity resv : allReservations) {
            if (Boolean.TRUE.equals(resv.getActive())
                    && resv.getStatus() == ReservationStatus.ACTIVE
                    && resv.getEndTime() != null
                    && now.isAfter(resv.getEndTime())) {

                resv.setStatus(ReservationStatus.COMPLETED);
                reservationRepository.save(resv);

                String keyInfo = resv.getKey() != null ? " — chave " + resv.getKey().getCode() : "";
                reservationHistoryService.recordHistory(
                        resv,
                        ReservationHistoryAction.COMPLETED,
                        "Reserva encerrada automaticamente pelo término do horário" + keyInfo,
                        false
                );

                log.info("Reserva {} encerrada automaticamente por término de horário ({})",
                        resv.getId(), resv.getEndTime());
            }
        }

        // 3. Recalcula e sincroniza o status de cada chave com base nas reservas ativas no minuto atual
        List<KeyEntity> allKeys = keyRepository.findAll();
        for (KeyEntity key : allKeys) {
            if (!Boolean.TRUE.equals(key.getActive())) continue;

            boolean inUseNow = allReservations.stream()
                    .filter(r -> Boolean.TRUE.equals(r.getActive())
                            && r.getStatus() == ReservationStatus.ACTIVE
                            && r.getKey() != null
                            && r.getKey().getId().equals(key.getId()))
                    .anyMatch(r -> {
                        LocalDateTime start = r.getStartTime() != null ? r.getStartTime() : now;
                        LocalDateTime end = r.getEndTime() != null ? r.getEndTime() : start.plusHours(1);
                        return !now.isBefore(start) && now.isBefore(end);
                    });

            boolean expectedAvailable = !inUseNow;
            if (expectedAvailable != Boolean.TRUE.equals(key.getAvailable())) {
                key.setAvailable(expectedAvailable);
                keyRepository.save(key);
                log.info("Chave {} ({}) sincronizada para available={}",
                        key.getCode(), key.getId(), expectedAvailable);
            }
        }
    }
}
