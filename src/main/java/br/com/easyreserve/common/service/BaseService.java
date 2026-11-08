package br.com.easyreserve.common.service;

import java.util.List;

public interface BaseService <TRegisterDto, TUpdateDto, TGetDto, TDeleteDto>{
    TGetDto add(TRegisterDto dto);
    List<TGetDto> findAll();
    void delete(TDeleteDto dto);
    TGetDto update(TUpdateDto dto);
}