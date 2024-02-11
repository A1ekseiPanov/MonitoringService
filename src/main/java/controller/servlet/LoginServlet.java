package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entity.dto.UserDto;
import entity.User;
import exception.InputDataConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repository.jdbc.JdbcUserRepository;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

import static controller.servlet.LoginServlet.LOGIN_PATH;
import static jakarta.servlet.http.HttpServletResponse.*;

/**
 * Сервлет для аутентификации пользователя.
 */
@WebServlet(LOGIN_PATH)
public class LoginServlet extends HttpServlet {
    public static final String LOGIN_PATH = "/login";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectNode responseNode = objectMapper.createObjectNode();
    private final UserService userService = new UserService(new JdbcUserRepository());

    /**
     * Обрабатывает POST запросы для аутентификации пользователя.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws ServletException если произошла ошибка сервлета
     * @throws IOException      если произошла ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader();
             Stream<String> lines = reader.lines()) {
            lines.forEach(sb::append);
        }
        String json = sb.toString();
        UserDto userDto = objectMapper.readValue(json, UserDto.class);
        try {
            User user = userService.login(userDto.getUsername(), userDto.getPassword());
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            printMessage("error", "Дорбро пожавловыать " +
                    userDto.getUsername(), SC_OK, resp);
        } catch (IllegalArgumentException e) {
            printMessage("error", e.getMessage(), SC_BAD_REQUEST, resp);
        } catch (InputDataConflictException e) {
            printMessage("error", e.getMessage(), SC_CONFLICT, resp);
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