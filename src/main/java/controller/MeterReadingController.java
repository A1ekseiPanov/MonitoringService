package controller;

import entity.MeterReading;
import entity.TypeMeterReading;
import exception.InputDataConflictException;
import exception.NotFoundException;
import service.MeterReadingService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MeterReadingController {
    private final MeterReadingService meterReadingService = MeterReadingService.getInstance();
    private static final MeterReadingController INSTANCE = new MeterReadingController();

    private MeterReadingController() {
    }

    public static MeterReadingController getInstance() {
        return INSTANCE;
    }

    public List<MeterReading> getAllReadings() {
        try {
            return meterReadingService.getReadingHistory();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<MeterReading> getLatestReadingsByTypes() {
        try {
            return meterReadingService.getLatestReadingsByTypes();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    public void submitMeterReading(TypeMeterReading typeMeterReading, BigDecimal reading) {
        try {
            meterReadingService.submitMeterReading(typeMeterReading, reading);
        } catch (InputDataConflictException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<MeterReading> getAllMeterReadingsByMonth(int month, int year) {
        try {
            return meterReadingService.getAllMeterReadingsByMonth(month, year);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }
}