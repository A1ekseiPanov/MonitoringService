package ru.panov.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panov.domain.model.Audit;
import ru.panov.domain.model.Role;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.TypeMeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;
import ru.panov.service.AuditService;
import ru.panov.service.MeterReadingService;
import ru.panov.service.TypeMeterReadingService;
import ru.panov.service.UserService;

import java.util.List;

import static ru.panov.controller.AdminController.ADMIN_PATH;
import static ru.panov.controller.TypeMeterReadingController.TYPE_PATH;

/**
 * Контроллер, отвечающий за операции, доступные администратору.
 */
@RestController
@RequestMapping(ADMIN_PATH)
@AllArgsConstructor
public class AdminController {
    private final MeterReadingService meterReadingService;
    private final AuditService auditService;
    private final UserService userService;
    private final TypeMeterReadingService typeMeterReadingService;
    public static final String ADMIN_PATH = "/admin";

    /**
     * Получение всех показаний счетчиков.
     *
     * @param session сессия пользователя
     * @return ответ с показаниями счетчиков
     */
    @GetMapping(MeterReadingController.METER_READING_PATH)
    public ResponseEntity<List<MeterReadingResponseDTO>> getAllMeterReading(HttpSession session) {
        UserResponseDTO userDto = (UserResponseDTO) session.getAttribute("user");
        User user = userService.getById(userDto.getId());
        if (user.getRole().equals(Role.ADMIN.toString())) {
            return ResponseEntity.ok(meterReadingService.getAll(user.getId()));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Создание нового типа показаний счетчика.
     *
     * @param type    информация о типе показаний
     * @param session сессия пользователя
     * @return ответ о результате операции
     */
    @PostMapping(value = TYPE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTypeMeterReading(@RequestBody TypeMeterReadingRequestDTO type, HttpSession session) {
        UserResponseDTO userDto = (UserResponseDTO) session.getAttribute("user");
        User user = userService.getById(userDto.getId());
        if (user.getRole().equals(Role.ADMIN.toString())) {
            typeMeterReadingService.addingType(type);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получение всех записей аудита.
     *
     * @param session сессия пользователя
     * @return ответ с записями аудита
     */
    @GetMapping("/audit")
    public ResponseEntity<List<Audit>> getAllAudits(HttpSession session) {
        UserResponseDTO userDto = (UserResponseDTO) session.getAttribute("user");
        User user = userService.getById(userDto.getId());
        if (user.getRole().equals(Role.ADMIN.toString())) {
            return ResponseEntity.ok(auditService.getAll());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}