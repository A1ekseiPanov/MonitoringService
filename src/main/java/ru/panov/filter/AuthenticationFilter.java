package ru.panov.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static ru.panov.controller.LoginController.LOGIN_PATH;
import static ru.panov.controller.RegistrationController.REGISTRATION_PATH;

/**
 * Фильтр аутентификации, который обеспечивает защиту от неавторизованного доступа к ресурсам.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    public static final String SWAGGER_UI_PATH = "/swagger-ui";
    public static final String SWAGGER_API_DOCS_PATH = "/v3/api-docs";
    private final Set<String> PUBLIC_PATH = Set.of(LOGIN_PATH, REGISTRATION_PATH,
            SWAGGER_UI_PATH, SWAGGER_API_DOCS_PATH);


    /**
     * Метод, который выполняет фильтрацию запросов.
     *
     * @param request  запрос от клиента
     * @param response ответ для клиента
     * @param filter   цепочка фильтров
     * @throws IOException      если возникает ошибка ввода-вывода
     * @throws ServletException если возникает ошибка сервлета
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filter) throws IOException, ServletException {

        String uri = ((HttpServletRequest) request).getRequestURI();
        System.out.println(uri);
        if (isPublicPath(uri) || isUserLoggedIn(request)) {
            filter.doFilter(request, response);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.put("error", "Пользователь не авторизован");
            response.getWriter().write(objectMapper.writeValueAsString(responseNode));
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Проверяет, является ли указанный URI публичным.
     *
     * @param uri URI для проверки
     * @return true, если URI является публичным, иначе false
     */
    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }

    /**
     * Проверяет, авторизован ли пользователь.
     *
     * @param request запрос от клиента
     * @return true, если пользователь авторизован, иначе false
     */
    private boolean isUserLoggedIn(ServletRequest request) {
        Object user = ((HttpServletRequest) request).getSession().getAttribute("user");
        return user != null;
    }
}