package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.dto.MeterReadingDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.MeterReadingRepository;
import repository.TypeMeterReadingRepository;
import repository.UserRepository;
import service.MeterReadingService;
import service.TypeMeterReadingService;

import java.io.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static util.TestData.*;

class MeterReadingServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private MeterReadingRepository meterReadingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TypeMeterReadingRepository typeMeterReadingRepository;

    @Mock
    private TypeMeterReadingService typeMeterReadingService;

    @Mock
    private MeterReadingService meterReadingService;

    @InjectMocks
    private MeterReadingServlet servlet;
    StringWriter stringWriter = new StringWriter();

    PrintWriter writer = new PrintWriter(stringWriter);
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        stringWriter.flush();
        writer.flush();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void afterEach() throws IOException {
        stringWriter.close();
        writer.close();
    }

    @Test
    void doGetAllTest() throws Exception {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(ADMIN);
        when(meterReadingService.getReadingHistory(ADMIN.getId()))
                .thenReturn(METER_READINGS);

        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        objectMapper.registerModule(new JavaTimeModule());
        String exs = objectMapper.writeValueAsString(METER_READINGS);
        writer.flush();
        String acc = stringWriter.toString().trim();
        assertEquals(exs, acc);
    }

    @Test
    void doPost() throws IOException {
        MeterReadingDto meterReadingDto = new MeterReadingDto(1L,BigDecimal.valueOf(100));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(USER1);
        when(typeMeterReadingService.getById(anyLong())).thenReturn(TYPE_METER_READING1);
        when(request.getReader()).thenReturn(
                new BufferedReader(
                        new StringReader(objectMapper.writeValueAsString(meterReadingDto))));

        when(response.getWriter()).thenReturn(writer);
        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}