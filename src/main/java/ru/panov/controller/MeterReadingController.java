package ru.panov.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;
import ru.panov.service.MeterReadingService;

import java.util.List;

import static ru.panov.controller.MeterReadingController.METER_READING_PATH;

/**
 * Контроллер для работы с показаниями счетчиков.
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = METER_READING_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class MeterReadingController {
    private final MeterReadingService service;
    public static final String METER_READING_PATH = "/meter_readings";

    /**
     * Получение всех показаний счетчиков для текущего пользователя.
     *
     * @param session сессия пользователя
     * @return список показаний счетчиков
     */
    @GetMapping
    public List<MeterReadingResponseDTO> getAll(HttpSession session) {
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        return service.getAll(user.getId());
    }

    /**
     * Отправка показаний счетчика.
     *
     * @param dto     запрос с показаниями счетчика
     * @param session сессия пользователя
     * @return ответ о создании показаний счетчика
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitMeterReading(@RequestBody MeterReadingRequestDTO dto,
                                                HttpSession session) {
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        service.submitMeterReading(dto, user.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получение последних показаний счетчиков для текущего пользователя.
     *
     * @param session сессия пользователя
     * @return список последних показаний счетчиков
     */
    @GetMapping("/last")
    public List<MeterReadingResponseDTO> getLast(HttpSession session) {
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        return service.getLatest(user.getId());
    }

    /**
     * Получение всех показаний счетчиков за указанный месяц и год для текущего пользователя.
     *
     * @param month   месяц
     * @param year    год
     * @param session сессия пользователя
     * @return список показаний счетчиков за указанный месяц и год
     */
    @GetMapping("/date")
    public List<MeterReadingResponseDTO> getAllMeterReadingsByMonth(@RequestParam int month,
                                                                    @RequestParam int year,
                                                                    HttpSession session) {
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        return service.getAllMeterReadingsByMonth(month, year, user.getId());
    }
}