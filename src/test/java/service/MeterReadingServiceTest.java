package service;

import exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MeterReadingServiceTest {
    private MeterReadingService meterReadingService;

    @BeforeEach
    void setUp() {
        this.meterReadingService = MeterReadingService.getInstance();
    }

    @Test
    void getReadingHistory_UserNotLoggedIn_ThrowNotFoundException() {
        assertThatThrownBy(() -> meterReadingService.getReadingHistory())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь не вошел в систему");
    }
}