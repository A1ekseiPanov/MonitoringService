package ru.panov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.panov.MonitoringServiceApp;
import ru.panov.service.MeterReadingService;
import ru.panov.service.TypeMeterReadingService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.panov.controller.MeterReadingController.*;
import static util.TestData.*;

@SpringBootTest(classes = MonitoringServiceApp.class)
@AutoConfigureMockMvc
class MeterReadingControllerTest {
    @MockBean
    private MeterReadingService meterReadingService;
    @MockBean
    private TypeMeterReadingService typeMeterReadingService;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Тест получения всех показаний счетчиков")
    @WithUserDetails(value = "admin")
    void getAllMeterReadingTest() throws Exception {
        mockMvc.perform(get(METER_READING_PATH))
                .andExpect(status().isOk());

        verify(meterReadingService, times(1)).getAll(ADMIN.getId());
    }

    @Test
    @DisplayName("Тест отправки показаний счетчика")
    @WithUserDetails(value = "user2")
    void submitMeterReadingTest() throws Exception {
        String json = objectMapper.writeValueAsString(METER_READING1_REQUEST_DTO);

        mockMvc.perform(post(METER_READING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(meterReadingService, times(1)).submitMeterReading(any(), eq(USER2.getId()));
    }

    @Test
    @DisplayName("Тест отправки показаний счетчика админом")
    @WithUserDetails(value = "admin")
    void submitMeterReadingAccessDeniedExceptionTest() throws Exception {
        String json = objectMapper.writeValueAsString(METER_READING1_REQUEST_DTO);

        mockMvc.perform(post(METER_READING_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccessDeniedException));
    }

    @Test
    @DisplayName("Тест получения последних показаний счетчиков")
    @WithUserDetails(value = "user2")
    void getLastMeterReadingTest() throws Exception {
        mockMvc.perform(get(METER_READING_PATH + METER_READING_LSAT_PATH))
                .andExpect(status().isOk());

        verify(meterReadingService, times(1)).getLatest(USER2.getId());
    }

    @Test
    @DisplayName("Тест добавления нового типа счетчика")
    @WithUserDetails(value = "admin")
    void addingTypeTest() throws Exception {
        String json = objectMapper.writeValueAsString(NEW_TYPE_METER_READING_REQUEST_DTO);

        mockMvc.perform(post(METER_READING_PATH + METER_READING_TYPES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(typeMeterReadingService, times(1)).addingType(any());
    }
}