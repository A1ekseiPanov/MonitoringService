package ru.panov.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;
import ru.panov.domain.responseDTO.TypeMeterReadingResponseDTO;
import ru.panov.service.MeterReadingService;
import ru.panov.service.TypeMeterReadingService;

import java.time.LocalDate;
import java.util.List;

import static ru.panov.controller.MeterReadingController.METER_READING_PATH;

/**
 * Контроллер для работы с показаниями и типами счетчиков.
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = METER_READING_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Meter reading Controller", description = "Контроллер для работы с показаниями счетчиков")
public class MeterReadingController {
    private final MeterReadingService service;
    private final TypeMeterReadingService typeMeterReadingService;
    public static final String METER_READING_PATH = "/meter_readings";
    public static final String METER_READING_LSAT_PATH = "/last";
    public static final String METER_READING_DATE_PATH = "/date";
    public static final String METER_READING_TYPES_PATH = "/types";

    /**
     * Получить все показания счетчиков.
     *
     * @param user Аутентифицированный пользователь
     * @return Список показаний счетчиков
     */
    @GetMapping
    @Operation(summary = "Все показания счетчиков")
    public List<MeterReadingResponseDTO> getAll(@AuthenticationPrincipal User user) {
        return service.getAll(user.getId());
    }

    /**
     * Передать показания счетчиков.
     *
     * @param dto  Данные запроса о показаниях счетчиков
     * @param user Аутентифицированный пользователь
     * @return Ответ на запрос
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Передача показаний счетчиков. Требуемая роль: User")
    public ResponseEntity<Void> submitMeterReading(@RequestBody @Valid MeterReadingRequestDTO dto,
                                                   @AuthenticationPrincipal User user) {
        service.submitMeterReading(dto, user.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получить последние показания счетчиков по каждому типу.
     *
     * @param user Аутентифицированный пользователь
     * @return Список последних показаний счетчиков
     */
    @GetMapping(METER_READING_LSAT_PATH)
    @Operation(summary = "Последние показания счетчиков по каждому типу")
    public List<MeterReadingResponseDTO> getLast(@AuthenticationPrincipal User user) {
        return service.getLatest(user.getId());
    }

    /**
     * Выборка показаний по месяцу и году.
     *
     * @param month Месяц
     * @param year  Год
     * @param user  Аутентифицированный пользователь
     * @return Список показаний счетчиков
     * @throws IllegalArgumentException Если год находится за пределами допустимого диапазона
     */
    @GetMapping(METER_READING_DATE_PATH)
    @Operation(summary = "Выборка показаний по месяцу и году")
    public List<MeterReadingResponseDTO> getAllMeterReadingsByMonth(@RequestParam @Min(1) @Max(12) int month,
                                                                    @RequestParam int year,
                                                                    @AuthenticationPrincipal User user) {
        int currentYear = LocalDate.now().getYear();
        if (year < 1970 || year > currentYear) {
            throw new IllegalArgumentException("Год должен быть в диапазоне от 1970 до " + currentYear);
        }
        return service.getAllMeterReadingsByMonth(month, year, user.getId());
    }

    /**
     * Добавление типа счетчика.
     *
     * @param type Данные запроса о типе счетчика
     * @return Ответ на запрос
     */
    @PostMapping(value = METER_READING_TYPES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Добавление типа счетчика. Требуемая роль: Admin")
    public ResponseEntity<Void> createTypeMeterReading(@RequestBody @Valid TypeMeterReadingRequestDTO type) {
        typeMeterReadingService.addingType(type);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получение всеч типов счетчиков.
     *
     * @return Список типов счетчиков
     */
    @GetMapping(METER_READING_TYPES_PATH)
    @Operation(summary = "Все типы счетчиков")
    public List<TypeMeterReadingResponseDTO> getAll() {
        return typeMeterReadingService.getAll();
    }
}