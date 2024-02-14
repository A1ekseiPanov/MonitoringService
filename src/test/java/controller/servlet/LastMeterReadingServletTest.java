package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.MeterReadingService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestData.LAST_METER_READINGS;
import static util.TestData.USER1;

class LastMeterReadingServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private MeterReadingService meterReadingService;

    @InjectMocks
    private LastMeterReadingServlet servlet;

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
    void testDoGet() throws Exception {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(USER1);
        when(meterReadingService.getLatestReadingsByTypes(USER1.getId()))
                .thenReturn(LAST_METER_READINGS);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        objectMapper.registerModule(new JavaTimeModule());
        String expectedJson = objectMapper.writeValueAsString(LAST_METER_READINGS);
        writer.flush();
        String actualJson = stringWriter.toString().trim();
        assertEquals(expectedJson, actualJson);
    }
}