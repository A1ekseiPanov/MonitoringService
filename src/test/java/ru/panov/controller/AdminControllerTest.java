package ru.panov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.panov.config.WebConfig;
import ru.panov.domain.responseDTO.UserResponseDTO;
import ru.panov.service.AuditService;
import ru.panov.service.MeterReadingService;
import ru.panov.service.TypeMeterReadingService;
import ru.panov.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.TestData.ADMIN;
import static util.TestData.USER1;

@SpringJUnitWebConfig(classes = WebConfig.class)
class AdminControllerTest {
    @Mock
    private MeterReadingService meterReadingService;

    @Mock
    private AuditService auditService;

    @Mock
    private UserService userService;

    @Mock
    private TypeMeterReadingService typeMeterReadingService;
    private MockMvc mockMvc;

    private AdminController adminController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(meterReadingService, auditService, userService, typeMeterReadingService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("Тест получения всех показаний счетчиков админом")
    void getAllMeterReadingTest() throws Exception {
        UserResponseDTO admin = new UserResponseDTO(ADMIN.getId(), ADMIN.getUsername());
        when(userService.getById(admin.getId())).thenReturn(ADMIN);

        mockMvc.perform(get("/admin/meter_readings")
                        .sessionAttr("user", admin))

                .andExpect(status().isOk());

        verify(meterReadingService, times(1)).getAll(any());
    }

    @Test
    @DisplayName("Тест создания нового типа показаний счетчика для пользователя без прав администратора")
    void createTypeMeterReadingNonAdminRoleTest() throws Exception {
        mockMvc.perform(post("/admin/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .sessionAttr("user", USER1))
                .andExpect(status().isNotFound());

        verify(typeMeterReadingService, never()).addingType(any());
    }

    @Test
    @DisplayName("Тест получения всех записей журнала аудита")
    void getAllAuditsTest() throws Exception {
        UserResponseDTO admin = new UserResponseDTO(ADMIN.getId(), ADMIN.getUsername());
        when(userService.getById(admin.getId())).thenReturn(ADMIN);

        mockMvc.perform(get("/admin/audit")
                        .sessionAttr("user", admin))
                .andExpect(status().isOk());

        verify(auditService, times(1)).getAll();
    }

    @Test
    @DisplayName("Тест получения всех записей журнала аудита для пользователя без прав администратора")
    void getAllAuditsNonAdminRoleTest() throws Exception {
        UserResponseDTO user = new UserResponseDTO(USER1.getId(), USER1.getUsername());
        when(userService.getById(user.getId())).thenReturn(USER1);

        mockMvc.perform(get("/admin/audit")
                        .sessionAttr("user", user))
                .andExpect(status().isNotFound());

        verify(auditService, never()).getAll();
    }
}