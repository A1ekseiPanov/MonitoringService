package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entity.dto.TypeMeterReadingDto;
import entity.Role;
import entity.TypeMeterReading;
import entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repository.TypeMeterReadingRepository;
import repository.jdbc.JdbcTypeMeterReadingRepository;
import service.TypeMeterReadingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static controller.servlet.TypeMeterReadingServlet.TYPE_METER_READING_PATH;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Сервлет для работы с типами счетчиков.
 */
@WebServlet(TYPE_METER_READING_PATH)
public class TypeMeterReadingServlet extends HttpServlet {
    public static final String TYPE_METER_READING_PATH = MeterReadingServlet.METER_READING_PATH + "/types";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ObjectNode responseNode = objectMapper.createObjectNode();
    private final TypeMeterReadingRepository typeMeterReadingRepository = new JdbcTypeMeterReadingRepository();
    private final TypeMeterReadingService typeMeterReadingService = new TypeMeterReadingService(typeMeterReadingRepository);

    /**
     * Обрабатывает GET запросы для получения всех типов счетчиков.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws IOException      если произошла ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        List<TypeMeterReading> typeMeterReadings = typeMeterReadingService.getAll();
        resp.getWriter().write(objectMapper.writeValueAsString(typeMeterReadings));
    }

    /**
     * Обрабатывает POST запросы для добавления нового типа счетчика.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if(user.getRole().equals(Role.ADMIN.toString())) {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String json = sb.toString();
            TypeMeterReadingDto typeMeterReadingDto = objectMapper.readValue(json, TypeMeterReadingDto.class);
            typeMeterReadingService.addingType(typeMeterReadingDto.getTitle());
            printMessage("message", "Тип счетчика " +
                    typeMeterReadingDto.getTitle() + " успешно добавлен", SC_CREATED, resp);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }


    /**
     * Выводит сообщение в формате JSON и устанавливает статус ответа.
     *
     * @param fieldName имя поля сообщения
     * @param message    текст сообщения
     * @param status     статус ответа
     * @param resp       HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private void printMessage(String fieldName, String message, int status, HttpServletResponse resp) throws IOException {
        responseNode.removeAll();
        responseNode.put(fieldName, message);
        resp.getWriter().write(objectMapper.writeValueAsString(responseNode));
        resp.setStatus(status);
    }
}
