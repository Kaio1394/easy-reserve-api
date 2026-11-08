package br.com.easyreserve.features.reservation_history.controller;

import br.com.easyreserve.features.reservation_history.dto.ReservationHistoryGetDto;
import br.com.easyreserve.features.reservation_history.service.ReservationHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/reservation-histories")
public class ReservationHistoryController {

    private final ReservationHistoryService reservationHistoryService;

    public ReservationHistoryController(ReservationHistoryService reservationHistoryService) {
        this.reservationHistoryService = reservationHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationHistoryGetDto>> findAll() {
        return ResponseEntity.ok(reservationHistoryService.findAll());
    }

    @GetMapping("/environment/{environmentId}")
    public ResponseEntity<List<ReservationHistoryGetDto>> findByEnvironmentId(@PathVariable UUID environmentId) {
        return ResponseEntity.ok(reservationHistoryService.findByEnvironmentId(environmentId));
    }
}
