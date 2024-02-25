package ru.panov.service.impl;

import annotations.Audit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;
import ru.panov.domain.responseDTO.TypeMeterReadingResponseDTO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.mapper.TypeMapper;
import ru.panov.repository.TypeMeterReadingRepository;
import ru.panov.service.TypeMeterReadingService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления типами счетчиков.
 */
@AllArgsConstructor
@Audit
@Service
public class TypeMeterReadingServiceImpl implements TypeMeterReadingService {
    private final TypeMeterReadingRepository typeMeterReadingRepository;
    private final TypeMapper mapper;

    /**
     * Добавляет новый тип счетчика.
     *
     * @param type информация о новом типе счетчика
     * @throws InputDataConflictException если тип счетчика уже существует
     */
    @Override
    public void addingType(TypeMeterReadingRequestDTO type) {
        if (getByTitle(type.getTitle()).isPresent()) {
            throw new InputDataConflictException("Такой тип счетчика уже существует");
        }
        TypeMeterReading typeMeterReading = mapper.requestDtoToEntity(type);
        typeMeterReadingRepository.save(typeMeterReading);
    }

    /**
     * Получает список всех типов счетчиков.
     *
     * @return список всех типов счетчиков
     */
    @Override
    public List<TypeMeterReadingResponseDTO> getAll() {
        return mapper.mapEntityToResponseDto(typeMeterReadingRepository.findAll());
    }

    /**
     * Получает тип счетчика по его идентификатору.
     *
     * @param id идентификатор типа счетчика
     * @return тип счетчика
     * @throws NotFoundException если тип счетчика не найден
     */
    @Override
    public TypeMeterReading getById(Long id) {
        return typeMeterReadingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Тип счетчика (id=%s) отсутствует", id)));
    }

    /**
     * Получает тип счетчика по его названию.
     */
    private Optional<TypeMeterReading> getByTitle(String title) {
        return typeMeterReadingRepository.findByTitle(title);
    }
}