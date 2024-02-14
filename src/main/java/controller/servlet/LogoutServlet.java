package controller.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static controller.servlet.LogoutServlet.*;

/**
 * Сервлет для выхода пользователя из системы.
 */
@WebServlet(LOGOUT_PATH)
public class LogoutServlet extends HttpServlet {
    public static final String LOGOUT_PATH = "/logout";

    /**
     * Обрабатывает GET запросы для выхода пользователя из системы.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.setAttribute("user", null);
    }
}