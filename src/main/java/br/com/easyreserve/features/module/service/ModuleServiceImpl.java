package br.com.easyreserve.features.module.service;

import br.com.easyreserve.exception.ModuleAlreadyExistsException;
import br.com.easyreserve.exception.ModuleNotFoundException;
import br.com.easyreserve.features.module.dto.ModuleCreateDto;
import br.com.easyreserve.features.module.dto.ModuleDeleteDto;
import br.com.easyreserve.features.module.dto.ModuleGetDto;
import br.com.easyreserve.features.module.dto.ModuleUpdateDto;
import br.com.easyreserve.features.module.entity.ModuleEntity;
import br.com.easyreserve.features.module.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    @Override
    public ModuleGetDto add(ModuleCreateDto moduleCreateDto) {
        if (moduleRepository.findModuleBySlug(moduleCreateDto.slug()).isPresent()) {
            throw new ModuleAlreadyExistsException(moduleCreateDto.slug());
        }
        ModuleEntity mEntity = moduleCreateDto.toEntity();
        mEntity = moduleRepository.save(mEntity);
        return ModuleGetDto.fromEntity(mEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ModuleGetDto> findAll() {
        return moduleRepository
                .findAll()
                .stream()
                .filter(m -> Boolean.TRUE.equals(m.getActive()))
                .sorted(Comparator.comparing(ModuleEntity::getSlug))
                .map(ModuleGetDto::fromEntity)
                .toList();
    }

    @Transactional
    @Override
    public void delete(ModuleDeleteDto moduleDeleteDto) {
        ModuleEntity mEntity = moduleRepository.findModuleBySlug(moduleDeleteDto.slug())
                .orElseThrow(() -> new ModuleNotFoundException(moduleDeleteDto.slug()));
        moduleRepository.delete(mEntity);
    }

    @Transactional
    @Override
    public ModuleGetDto update(ModuleUpdateDto moduleUpdateDto) {
        ModuleEntity mEntity = moduleRepository.findModuleBySlug(moduleUpdateDto.slug())
                .orElseThrow(() -> new ModuleNotFoundException(moduleUpdateDto.slug()));
        mEntity.setSlug(moduleUpdateDto.slug());
        if (moduleUpdateDto.name() != null && !moduleUpdateDto.name().isBlank()) {
            mEntity.setName(moduleUpdateDto.name());
        }
        mEntity.setActive(moduleUpdateDto.active());
        mEntity = moduleRepository.save(mEntity);
        return ModuleGetDto.fromEntity(mEntity);
    }
}
