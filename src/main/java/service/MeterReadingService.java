package service;

import entity.MeterReading;
import entity.TypeMeterReading;
import entity.User;
import exception.InputDataConflictException;
import exception.NotFoundException;
import repository.MeterReadingRepository;
import repository.memory.MemoryMeterReadingRepository;
import util.AuditLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MeterReadingService {
    private final UserService userService = UserService.getInstance();
    private final MeterReadingRepository meterReadingRepository = MemoryMeterReadingRepository.getInstance();
    private static final MeterReadingService INSTANCE = new MeterReadingService();

    private MeterReadingService() {
    }

    public static MeterReadingService getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает список всех показаний счетчиков для вошедшего в систему пользователя.
     *
     * @return Список всех показаний счетчиков для текущего пользователя
     * @throws NotFoundException если пользователь не вошел в систему или у него нет показаний
     */
    public List<MeterReading> getReadingHistory() {
        User loggedUser = userService.getLoggedUser();
        if (loggedUser == null) {
            throw new NotFoundException("Пользователь не вошел в систему");
        }
        List<MeterReading> userReadings = null;
        try {
            userReadings = meterReadingRepository
                    .findAllMeterReadingByUserId(loggedUser.getId());
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (userReadings.isEmpty()) {
            throw new NotFoundException("Показаний нет");
        }
        return userReadings;
    }

    /**
     * Возвращает список последних показаний счетчиков по каждому типу.
     *
     * @return Список последних показаний счетчиков по каждому типу
     * @throws NotFoundException если пользователь не вошел в систему или у него нет показаний
     */
    public List<MeterReading> getLatestReadingsByTypes() {
        List<TypeMeterReading> meterTypes = List.of(TypeMeterReading.values());

        return meterTypes.stream().map(meterType -> getReadingHistory().stream()
                        .filter(reading -> reading.getType() == meterType)
                        .max(Comparator.comparing(MeterReading::getLocalDate))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Отправляет показание счетчика для текущего пользователя.
     *
     * @param typeMeterReading Тип показания счетчика
     * @param reading          Значение показания счетчика
     * @throws InputDataConflictException если показание уже было отправлено в текущем месяце
     */
    public void submitMeterReading(TypeMeterReading typeMeterReading, BigDecimal reading) {
        User loggedUser = UserService.getInstance().getLoggedUser();
        MeterReading meterReading = new MeterReading(typeMeterReading, reading);
        List<MeterReading> meterReadings = loggedUser.getMeterReadings();
        if (meterReadings.isEmpty()) {
            try {
                meterReadingRepository.save(meterReading, loggedUser.getId());
                AuditLog.logAction("Пользователь username(" + loggedUser.getUsername()
                        + ") Передал показания " + typeMeterReading.getTitle()
                        + ": " + meterReading.getReading());
                return;
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        MeterReading filterByTypeMeterReadings = meterReadings.stream()
                .filter(mr -> mr.getType().equals(typeMeterReading)
                        && mr.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .findAny()
                .orElse(null);
        if (filterByTypeMeterReadings == null) {
            try {
                meterReadingRepository.save(meterReading, loggedUser.getId());
                AuditLog.logAction("Пользователь :" + loggedUser.getUsername()
                        + ", Передал показания " + typeMeterReading.getTitle()
                        + ": " + meterReading.getReading());
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new InputDataConflictException("Вы передавали показания по данному счетчику в этом месяце");
        }
    }

    /**
     * Возвращает список всех показаний счетчиков для текущего пользователя в указанном месяце и году.
     *
     * @param month Месяц
     * @param year  Год
     * @return Список всех показаний счетчиков для текущего пользователя в указанном месяце и году
     * @throws NotFoundException если пользователь не вошел в систему или в указанном месяце нет показаний
     */
    public List<MeterReading> getAllMeterReadingsByMonth(int month, int year) {
        User currentUser = UserService.getInstance().getLoggedUser();
        if (currentUser == null) {
            throw new NotFoundException("Войдите в систему");
        }
        List<MeterReading> meterReadings = meterReadingRepository
                .findAllMeterReadingByUserId(currentUser.getId());
        List<MeterReading> meterReading = meterReadings.stream()
                .filter(mr -> mr.getLocalDate().getMonthValue() == month
                        && mr.getLocalDate().getYear() == year)
                .toList();
        if (meterReading.isEmpty()) {
            throw new NotFoundException("В данном месяце нет показаний");
        }
        return meterReading;
    }
}