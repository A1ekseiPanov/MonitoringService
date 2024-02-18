package service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.panov.domain.requestDTO.MeterReadingRequestDTO;
import ru.panov.domain.responseDTO.MeterReadingResponseDTO;
import ru.panov.exception.NotFoundException;
import ru.panov.mapper.MeterReadingMapper;
import ru.panov.repository.MeterReadingRepository;
import ru.panov.service.TypeMeterReadingService;
import ru.panov.service.UserService;
import ru.panov.service.impl.MeterReadingServiceImpl;
import ru.panov.validator.MonthYearValidator;
import ru.panov.validator.Validator;
import ru.panov.validator.ValidatorResult;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static util.TestData.*;

class MeterReadingServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private MeterReadingRepository meterReadingRepository;
    @Mock
    private TypeMeterReadingService typeMeterReadingService;
    @Mock
    private Validator<MeterReadingRequestDTO> meterReadingValidator;
    @Mock
    private MonthYearValidator monthYearValidator;
    @Mock
    private MeterReadingMapper mapper;
    @InjectMocks
    private MeterReadingServiceImpl meterReadingService;
    @Mock
    private ValidatorResult validatorResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Получение истории показаний")
    void getReadingHistoryTest() {
        when(userService.getById(USER1.getId())).thenReturn(USER1);
        when(meterReadingRepository.findAllByUserId(USER1.getId())).thenReturn(METER_READINGS);
        when(mapper.toDtoResponseList(METER_READINGS)).thenReturn(any());

        List<MeterReadingResponseDTO> actualReadings = meterReadingService.getAll(USER1.getId());

        verify(userService, times(1)).getById(USER1.getId());
        verify(meterReadingRepository, times(1)).findAllByUserId(USER1.getId());
        verify(mapper, times(1)).toDtoResponseList(any());
    }

    @Test
    @DisplayName("Получение истории показаний, если история пуста")
    void getReadingHistoryIsEmptyTest() {
        Long userId = USER1.getId();

        when(userService.getById(userId)).thenReturn(USER1);
        when(meterReadingRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> meterReadingService.getAll(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Показаний нет");
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц")
    void getAllMeterReadingsByMonthTest() {
        when(monthYearValidator.isValid(anyInt(), anyInt())).thenReturn(new ValidatorResult());
        when(validatorResult.isValid()).thenReturn(true);
        when(meterReadingRepository
                .findAllByUserId(USER1.getId())).thenReturn(METER_READINGS);

        List<MeterReadingResponseDTO> result = meterReadingService
                .getAllMeterReadingsByMonth(LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear(), USER1.getId());

        verify(meterReadingRepository, times(1)).findAllByUserId(USER1.getId());
        verify(monthYearValidator, times(1)).isValid(anyInt(), anyInt());
        verify(mapper, times(1)).toDtoResponseList(any());
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц, если в указанный месяц нет показаний")
    void getAllMeterReadingsByMonthNoReadingsForMonthTest() {
        when(monthYearValidator.isValid(anyInt(), anyInt())).thenReturn(new ValidatorResult());
        when(validatorResult.isValid()).thenReturn(true);
        when(meterReadingRepository
                .findAllByUserId(USER1.getId())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> {
            meterReadingService.getAllMeterReadingsByMonth(MONTH, YEAR, USER1.getId());
        }).isInstanceOf(NotFoundException.class)
                .hasMessage("В данном месяце нет показаний");
    }
}