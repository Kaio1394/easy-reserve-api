package br.com.easyreserve.features.reservation.controller;

import br.com.easyreserve.features.reservation.dto.ReservationCreateDto;
import br.com.easyreserve.features.reservation.dto.ReservationDeleteDto;
import br.com.easyreserve.features.reservation.dto.ReservationGetDto;
import br.com.easyreserve.features.reservation.dto.ReservationUpdateDto;
import br.com.easyreserve.features.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationGetDto> add(@Valid @RequestBody ReservationCreateDto dto) {
        ReservationGetDto response = reservationService.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationGetDto>> findAll() {
        List<ReservationGetDto> response = reservationService.findAll();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ReservationGetDto> update(@Valid @RequestBody ReservationUpdateDto dto) {
        ReservationGetDto response = reservationService.update(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody ReservationDeleteDto dto) {
        reservationService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
