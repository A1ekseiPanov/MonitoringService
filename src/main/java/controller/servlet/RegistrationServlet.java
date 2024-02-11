package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import repository.UserRepository;
import repository.jdbc.JdbcUserRepository;
import entity.dto.UserDto;
import exception.InputDataConflictException;
import exception.ValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;

import static controller.servlet.RegistrationServlet.REGISTRATION_PATH;
import static jakarta.servlet.http.HttpServletResponse.*;

/**
 * Сервлет для обработки запросов на регистрацию новых пользователей.
 */
@WebServlet(REGISTRATION_PATH)
public class RegistrationServlet extends HttpServlet {
    public static final String REGISTRATION_PATH = "/registration";
    private final UserRepository userRepository = new JdbcUserRepository();
    private final UserService userService = new UserService(userRepository);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectNode responseNode = objectMapper.createObjectNode();

    /**
     * Обрабатывает POST запросы для регистрации новых пользователей.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();
        UserDto userDto = objectMapper.readValue(json, UserDto.class);
        try {
            userService.register(userDto.getUsername(), userDto.getPassword());
            printMessage("message", "Пользователь " +
                    userDto.getUsername() + " успешно зарегистрирован", SC_CREATED, resp);
        } catch (ValidationException e) {
            printMessage("validation errors", e.getErrors().toString(), SC_BAD_REQUEST, resp);

        } catch (InputDataConflictException e) {
            printMessage("error", e.getMessage(), SC_CONFLICT, resp);
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
