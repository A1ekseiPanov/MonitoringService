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
import static org.mockito.Mockito.when;
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
    @DisplayName("Получение истории показаний, если пользователь не вошел в систему")
    void getReadingHistoryUserNotLoggedInTest() {
        when(userService.getLoggedUser()).thenReturn(null);
        assertThatThrownBy(() -> meterReadingService.getReadingHistory())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь не вошел в систему");
    }

    @Test
    @DisplayName("Получение истории показаний")
    void getReadingHistoryTest() {
        when(meterReadingRepository
                .findAllMeterReadingByUserId(USER1.getId())).thenReturn(METER_READINGS);

        assertThat(meterReadingService.getReadingHistory()).isEqualTo(METER_READINGS);
    }

    @Test
    @DisplayName("Получение истории показаний, если история пуста")
    void getReadingHistoryIsEmptyTest() {
        when(meterReadingRepository
                .findAllMeterReadingByUserId(USER1.getId())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> meterReadingService.getReadingHistory())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Показаний нет");
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц")
    void getAllMeterReadingsByMonthTest() {
        when(meterReadingRepository.findAllMeterReadingByUserId(USER1.getId()))
                .thenReturn(METER_READINGS);

        List<MeterReading> result = meterReadingService
                .getAllMeterReadingsByMonth(LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear());

        assertThat(result).containsAnyElementsOf(METER_READINGS);
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц, если пользователь не вошел в систему")
    void getAllMeterReadingsByMonthUserNotLoggedInTest() {
        when(userService.getLoggedUser()).thenReturn(null);

        assertThatThrownBy(() -> {
            meterReadingService.getAllMeterReadingsByMonth(MONTH, YEAR);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage("Войдите в систему");
    }

    @Test
    @DisplayName("Получение всех показаний счетчиков за указанный месяц, если в указанный месяц нет показаний")
    void getAllMeterReadingsByMonthNoReadingsForMonthTest() {
        when(meterReadingRepository.findAllMeterReadingByUserId(USER1.getId()))
                .thenReturn(METER_READINGS);

        assertThatThrownBy(() -> {
            meterReadingService.getAllMeterReadingsByMonth(MONTH, YEAR);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage("В данном месяце нет показаний");
    }

    @Test
    @DisplayName("Подача показаний, если история пуста")
    void submitMeterReadingEmptyMeterReadings() {
        assertDoesNotThrow(() -> meterReadingService
                .submitMeterReading(TYPE_METER_READING1, BigDecimal.valueOf(234)));
    }
}