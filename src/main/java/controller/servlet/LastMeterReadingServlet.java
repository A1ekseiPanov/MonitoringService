package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.MeterReading;
import entity.User;
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

import java.io.IOException;
import java.util.List;

import static controller.servlet.LastMeterReadingServlet.LAST_PATH;

/**
 * Сервлет для получения последних показаний счетчиков.
 */
@WebServlet(LAST_PATH)
@AllArgsConstructor
public class LastMeterReadingServlet extends HttpServlet {
    public static final String LAST_PATH = MeterReadingServlet.METER_READING_PATH + "/last";
    private final TypeMeterReadingRepository typeMeterReadingRepository;
    private final TypeMeterReadingService typeMeterReadingService;
    private final JdbcUserRepository jdbcUserRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MeterReadingService meterReadingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ObjectNode responseNode = objectMapper.createObjectNode();

    public LastMeterReadingServlet() {
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
     * Обрабатывает GET запрос для получения последних показаний счетчиков.
     *
     * @param req  HTTP запрос
     * @param resp HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<MeterReading> lastMeterReadings = meterReadingService.getLatestReadingsByTypes(user.getId());
        objectMapper.registerModule(new JavaTimeModule());
        resp.getWriter().write(objectMapper.writeValueAsString(lastMeterReadings));
    }
}