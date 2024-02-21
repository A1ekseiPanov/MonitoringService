package ru.panov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.panov.domain.responseDTO.UserResponseDTO;
import ru.panov.service.MeterReadingService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.TestData.MONTH;
import static util.TestData.YEAR;

class MeterReadingControllerTest {
    @Mock
    private MeterReadingService meterReadingService;

    private MockMvc mockMvc;

    private MeterReadingController meterReadingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meterReadingController = new MeterReadingController(meterReadingService);
        mockMvc = MockMvcBuilders.standaloneSetup(meterReadingController).build();
    }
    @Test
    @DisplayName("Тест получения всех показаний счетчиков")
    void getAllMeterReadingTest() throws Exception {
        UserResponseDTO user = new UserResponseDTO(1L, "username");
        when(meterReadingService.getAll(user.getId())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/meter_readings")
                        .sessionAttr("user", user))
                .andExpect(status().isOk());

        verify(meterReadingService, times(1)).getAll(user.getId());
    }

    @Test
    @DisplayName("Тест отправки показаний счетчика")
    void submitMeterReadingTest() throws Exception {
        UserResponseDTO user = new UserResponseDTO(1L, "username");

        mockMvc.perform(post("/meter_readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .sessionAttr("user", user))
                .andExpect(status().isCreated());

        verify(meterReadingService, times(1)).submitMeterReading(any(), eq(user.getId()));
    }

    @Test
    @DisplayName("Тест получения последних показаний счетчиков")
    void getLastMeterReadingTest() throws Exception {
        UserResponseDTO user = new UserResponseDTO(1L, "username");
        when(meterReadingService.getLatest(user.getId())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/meter_readings/last")
                        .sessionAttr("user", user))
                .andExpect(status().isOk());

        verify(meterReadingService, times(1)).getLatest(user.getId());
    }

    @Test
    @DisplayName("Тест получения всех показаний счетчиков за указанный месяц и год")
    void getAllMeterReadingsByMonthTest() throws Exception {
        UserResponseDTO user = new UserResponseDTO(1L, "username");
        when(meterReadingService.getAllMeterReadingsByMonth(MONTH, YEAR, user.getId()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/meter_readings/date")
                        .param("month", String.valueOf(MONTH))
                        .param("year", String.valueOf(YEAR))
                        .sessionAttr("user", user))
                .andExpect(status().isOk());

        verify(meterReadingService, times(1)).getAllMeterReadingsByMonth(MONTH, YEAR, user.getId());
    }
}