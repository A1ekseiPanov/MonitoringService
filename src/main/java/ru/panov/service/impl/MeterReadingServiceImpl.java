package ru.panov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panov.annotations.Audit;
import ru.panov.domain.model.MeterReading;
import ru.panov.domain.model.Role;
import ru.panov.domain.model.TypeMeterReading;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;
import ru.panov.domain.responseDTO.TypeMeterReadingResponseDTO;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.MeterReadingMapper;
import ru.panov.repository.MeterReadingRepository;
import ru.panov.service.MeterReadingService;
import ru.panov.service.TypeMeterReadingService;
import ru.panov.service.UserService;
import ru.panov.validator.MonthYearValidator;
import ru.panov.validator.Validator;
import ru.panov.validator.ValidatorResult;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с показаниями счетчиков.
 */
@AllArgsConstructor
@Audit
@Service
public class MeterReadingServiceImpl implements MeterReadingService {
    private final UserService userService;
    private final MeterReadingRepository meterReadingRepository;
    private final TypeMeterReadingService typeMeterReadingService;
    private final Validator<MeterReadingRequestDTO> meterReadingValidator;
    private final MonthYearValidator monthYearValidator;
    private final MeterReadingMapper mapper;

    /**
     * Получает список всех показаний счетчиков для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список всех показаний счетчиков для пользователя
     * @throws NotFoundException если не удалось найти пользователя или показания счетчиков
     */
    public List<MeterReadingResponseDTO> getAll(Long userId) {
        User user = null;
        try {
            user = userService.getById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        List<MeterReading> userReadings = null;
        try {

            userReadings = Objects.equals(user.getRole(), Role.ADMIN.toString())
                    ? meterReadingRepository.findAll()
                    : meterReadingRepository.findAllByUserId(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        if (userReadings.isEmpty()) {
            throw new NotFoundException("Показаний нет");
        }
        return mapper.toDtoResponseList(userReadings);
    }


    /**
     * Возвращает список последних показаний счетчиков по каждому типу.
     *
     * @param userId идентификатор пользователя
     * @return Список последних показаний счетчиков по каждому типу
     */
    @Override
    public List<MeterReadingResponseDTO> getLatest(Long userId) {
        return typeMeterReadingService.getAll().stream()
                .map(type -> getLatestReadingByType(type, userId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Получает последнее показание счетчика заданного типа для пользователя.
     */
    private MeterReadingResponseDTO getLatestReadingByType(TypeMeterReadingResponseDTO type, Long userId) {
        return getAll(userId).stream()
                .filter(reading -> reading.getTypeMR().equals(type.getTitle()))
                .max(Comparator.comparing(MeterReadingResponseDTO::getLocalDate))
                .orElse(null);
    }

    /**
     * Метод для отправки показаний счетчика.
     *
     * @param dto    объект DTO с показаниями счетчика
     * @param userId идентификатор пользователя
     * @throws ValidationException        если данные не прошли валидацию
     * @throws InputDataConflictException если данные конфликтуют с уже имеющимися в базе данных
     */
    @Override
    public void submitMeterReading(MeterReadingRequestDTO dto, Long userId) {
        ValidatorResult validatorResult = meterReadingValidator.isValid(dto);
        if (!validatorResult.isValid()) {
            throw new ValidationException(validatorResult.getErrors().toString());
        }
        TypeMeterReading tmr = typeMeterReadingService.getById(dto.getTypeId());
        List<MeterReading> meterReadings = meterReadingRepository.findAllByUserId(userId);
        Optional<MeterReading> latestReading = findLatestReadingByType(meterReadings, dto.getTypeId());
        if (meterReadings.isEmpty() || filterByTypeMeterReadings(meterReadings, dto.getTypeId()).isEmpty()) {
            if (latestReading.isPresent() && dto.getReading().compareTo(latestReading.get().getReading()) <= 0) {
                throw new InputDataConflictException("Показания должны быть больше, чем предыдущие показания по данному счетчику");
            }
            try {
                meterReadingRepository.save(mapper.requestDTOtoEntity(dto), userId);
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
            }
        } else {
            throw new InputDataConflictException("Вы передавали показания по данному счетчику в этом месяце");
        }
    }

    /**
     * Фильтрует список показаний счетчиков по типу счетчика и текущему месяцу.
     */
    private Optional<MeterReading> filterByTypeMeterReadings(List<MeterReading> meterReadings, Long typeId) {
        return meterReadings.stream()
                .filter(mr -> (mr.getType().getId()).equals(typeId)
                        && mr.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .findAny();
    }

    /**
     * Находит последние показания по типу счетчика.
     */
    private Optional<MeterReading> findLatestReadingByType(List<MeterReading> meterReadings, Long typeId) {
        return meterReadings.stream()
                .filter(mr -> mr.getType().getId().equals(typeId))
                .max(Comparator.comparing(MeterReading::getLocalDate));
    }

    /**
     * Получает все показания счетчиков за указанный месяц и год для определенного пользователя.
     *
     * @param month  месяц
     * @param year   год
     * @param userId идентификатор пользователя
     * @return список DTO объектов с показаниями счетчиков за указанный месяц и год
     * @throws ValidationException если входные данные (месяц и/или год) не прошли валидацию
     * @throws NotFoundException   если в указанном месяце нет показаний
     */
    @Override
    public List<MeterReadingResponseDTO> getAllMeterReadingsByMonth(int month, int year, Long userId) {
        ValidatorResult valid = monthYearValidator.isValid(month, year);
        List<MeterReading> meterReadings = meterReadingRepository
                .findAllByUserId(userId);
        List<MeterReading> meterReading = null;
        if (valid.isValid()) {
            meterReading = meterReadings.stream()
                    .filter(mr -> mr.getLocalDate().getMonthValue() == month
                            && mr.getLocalDate().getYear() == year)
                    .toList();
        } else {
            throw new ValidationException(valid.getErrors().toString());
        }
        if (meterReading.isEmpty()) {
            throw new NotFoundException("В данном месяце нет показаний");
        }
        return mapper.toDtoResponseList(meterReading);
    }
}