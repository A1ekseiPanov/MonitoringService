package service;

import entity.MeterReading;
import exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.MeterReadingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static util.TestData.*;

class MeterReadingServiceTest {
    @InjectMocks
    private MeterReadingService meterReadingService;
    @Mock
    private UserService userService;
    @Mock
    private MeterReadingRepository meterReadingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userService.getLoggedUser()).thenReturn(USER1);
    }

    @Test
    @DisplayName("Получение истории показаний")
    void getReadingHistoryTest() {
        Long userId = USER1.getId();
        List<MeterReading> expectedReadings = METER_READINGS;
        when(userService.getById(userId)).thenReturn(USER1);
        when(meterReadingRepository.findAllByUserId(userId)).thenReturn(expectedReadings);

        List<MeterReading> actualReadings = meterReadingService.getReadingHistory(userId);

        assertThat(expectedReadings).isEqualTo(actualReadings);
        verify(userService, times(1)).getById(userId);
        verify(meterReadingRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Получение истории показаний, если история пуста")
    void getReadingHistoryIsEmptyTest() {
        Long userId = USER1.getId();

        when(userService.getById(userId)).thenReturn(USER1);
        when(meterReadingRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> meterReadingService.getReadingHistory(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Показаний нет");
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц")
    void getAllMeterReadingsByMonthTest() {
        when(meterReadingRepository.findAllByUserId(USER1.getId()))
                .thenReturn(METER_READINGS);

        List<MeterReading> result = meterReadingService
                .getAllMeterReadingsByMonth(LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear(), USER1.getId());

        assertThat(result).containsAnyElementsOf(METER_READINGS);
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц, если в указанный месяц нет показаний")
    void getAllMeterReadingsByMonthNoReadingsForMonthTest() {
        when(meterReadingRepository.findAllByUserId(USER1.getId()))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> {
            meterReadingService.getAllMeterReadingsByMonth(MONTH, YEAR, USER1.getId());
        }).isInstanceOf(NotFoundException.class)
                .hasMessage("В данном месяце нет показаний");
    }

    @Test
    @DisplayName("Подача показаний, если история пуста")
    void submitMeterReadingEmptyMeterReadings() {
        assertDoesNotThrow(() -> meterReadingService
                .submitMeterReading(TYPE_METER_READING1, BigDecimal.valueOf(234), USER1.getId()));
    }
}