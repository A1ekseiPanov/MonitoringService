package ru.panov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.domain.responseDTO.TypeMeterReadingResponseDTO;
import ru.panov.service.TypeMeterReadingService;

import java.util.List;

import static ru.panov.controller.MeterReadingController.METER_READING_PATH;
import static ru.panov.controller.TypeMeterReadingController.TYPE_PATH;

/**
 * Контроллер для работы с типами показаний счетчиков.
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = TYPE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class TypeMeterReadingController {
    private final TypeMeterReadingService service;
    public static final String TYPE_PATH = METER_READING_PATH + "/types";

    /**
     * Получение всех типов показаний счетчиков.
     *
     * @return список всех типов показаний счетчиков
     */
    @GetMapping
    public List<TypeMeterReadingResponseDTO> getAll() {
        return service.getAll();
    }
}