package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.dto.MeterReadingDto;
import entity.MeterReading;
import entity.TypeMeterReading;
import entity.User;
import exception.InputDataConflictException;
import exception.NotFoundException;
import exception.ValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import repository.MeterReadingRepository;
import repository.TypeMeterReadingRepository;
import repository.UserRepository;
import repository.jdbc.JdbcMeterReadingRepository;
import repository.jdbc.JdbcTypeMeterReadingRepository;
import repository.jdbc.JdbcUserRepository;
import service.MeterReadingService;
import service.TypeMeterReadingService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static controller.servlet.MeterReadingServlet.METER_READING_PATH;
import static jakarta.servlet.http.HttpServletResponse.*;


/**
 * Сервлет для обработки запросов на получение и добавление показаний счетчиков.
 */
@WebServlet(METER_READING_PATH)
@AllArgsConstructor
public class MeterReadingServlet extends HttpServlet {
    public static final String METER_READING_PATH = "/meter_readings";
    private final TypeMeterReadingRepository typeMeterReadingRepository;
    private final TypeMeterReadingService typeMeterReadingService;
    private final JdbcUserRepository jdbcUserRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MeterReadingService meterReadingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ObjectNode responseNode = objectMapper.createObjectNode();

    public MeterReadingServlet() {
        this.typeMeterReadingRepository =
                new JdbcTypeMeterReadingRepository();
        this.typeMeterReadingService =
                new TypeMeterReadingService(typeMeterReadingRepository);
        this.jdbcUserRepository =
                new JdbcUserRepository();
        this.meterReadingRepository =
                new JdbcMeterReadingRepository(jdbcUserRepository, typeMeterReadingRepository);
        this.userRepository = new JdbcUserRepository();
        this.userService = new UserService(userRepository);
        this.meterReadingService =
                new MeterReadingService(userService, meterReadingRepository, typeMeterReadingService);
    }

    /**
     * Обрабатывает GET запросы для получения истории показаний счетчиков.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(SC_OK);
        resp.setContentType("application/json");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String month = req.getParameter("month");
        String year = req.getParameter("year");
        List<MeterReading> meterReadings = null;
        objectMapper.registerModule(new JavaTimeModule());
        try {
            if (month == null & year == null) {
                meterReadings = meterReadingService.getReadingHistory(user.getId());
                resp.getWriter().write(objectMapper.writeValueAsString(meterReadings));
            } else {
                meterReadings = meterReadingService
                        .getAllMeterReadingsByMonth(Integer.parseInt(month), Integer.parseInt(year),
                                user.getId());
                resp.getWriter().write(objectMapper.writeValueAsString(meterReadings));
            }
        } catch (NotFoundException e) {
            printMessage("error", e.getMessage(), SC_NOT_FOUND, resp);
        } catch (ValidationException e) {
            printMessage("validation errors", e.getErrors().toString(), SC_BAD_REQUEST, resp);
        }
    }

    /**
     * Обрабатывает POST запросы для добавления новых показаний счетчиков.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader();
             Stream<String> lines = reader.lines()) {
            lines.forEach(sb::append);
        }
        String json = sb.toString();

        MeterReadingDto meterReadingDto = objectMapper.readValue(json, MeterReadingDto.class);
        try {
            TypeMeterReading type = typeMeterReadingService.getById(meterReadingDto.getTypeId());
            meterReadingService.submitMeterReading(type, meterReadingDto.getReading(), user.getId());
            responseNode.removeAll();
            printMessage("message", "Показыния добавлены успешно", SC_OK, resp);
        } catch (NotFoundException e) {
            printMessage("error", e.getMessage(), SC_NOT_FOUND, resp);
        } catch (InputDataConflictException e) {
            printMessage("error", e.getMessage(), SC_CONFLICT, resp);
        } catch (ValidationException e) {
            printMessage("validation errors", e.getErrors().toString(), SC_BAD_REQUEST, resp);
        }
    }

    /**
     * Выводит сообщение в формате JSON и устанавливает статус ответа.
     *
     * @param fieldName имя поля сообщения
     * @param message   текст сообщения
     * @param status    статус ответа
     * @param resp      HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private void printMessage(String fieldName, String message, int status, HttpServletResponse resp) throws IOException {
        responseNode.removeAll();
        responseNode.put(fieldName, message);
        resp.getWriter().write(objectMapper.writeValueAsString(responseNode));
        resp.setStatus(status);
    }
}