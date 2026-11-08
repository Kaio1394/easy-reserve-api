package br.com.easyreserve.features.reservation.service;

import br.com.easyreserve.common.service.BaseService;
import br.com.easyreserve.features.reservation.dto.ReservationCreateDto;
import br.com.easyreserve.features.reservation.dto.ReservationDeleteDto;
import br.com.easyreserve.features.reservation.dto.ReservationGetDto;
import br.com.easyreserve.features.reservation.dto.ReservationUpdateDto;

public interface ReservationService extends BaseService<ReservationCreateDto, ReservationUpdateDto, ReservationGetDto, ReservationDeleteDto> {
}
