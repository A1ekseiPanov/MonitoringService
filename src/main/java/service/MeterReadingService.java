package service;

import entity.MeterReading;
import entity.Role;
import entity.TypeMeterReading;
import entity.User;
import exception.InputDataConflictException;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import repository.MeterReadingRepository;
import util.AuditLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MeterReadingService {
    private final UserService userService;
    private final MeterReadingRepository meterReadingRepository;
    private final TypeMeterReadingService typeMeterReadingService;

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
            userReadings = loggedUser.getRole().equals(Role.ADMIN.toString())
                    ? meterReadingRepository.findAll()
                    : meterReadingRepository.findAllByUserId(loggedUser.getId());
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
        return typeMeterReadingService.getAll().stream()
                .map(this::getLatestReadingByType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MeterReading getLatestReadingByType(TypeMeterReading type) {
        return getReadingHistory().stream()
                .filter(reading -> reading.getType().equals(type))
                .max(Comparator.comparing(MeterReading::getLocalDate))
                .orElse(null);
    }

    /**
     * Отправляет показание счетчика для текущего пользователя.
     *
     * @param typeMeterReading Тип показания счетчика
     * @param reading          Значение показания счетчика
     * @throws InputDataConflictException если показание уже было отправлено в текущем месяце
     */
    public void submitMeterReading(TypeMeterReading typeMeterReading, BigDecimal reading) {
        User loggedUser = userService.getLoggedUser();
        MeterReading meterReading = new MeterReading(typeMeterReading, reading);
        List<MeterReading> meterReadings = meterReadingRepository.findAllByUserId(loggedUser.getId());
        if (meterReadings.isEmpty() || filterByTypeMeterReadings(meterReadings, typeMeterReading).isEmpty()) {
            try {
                meterReadingRepository.save(meterReading, loggedUser.getId());
                AuditLog.logAction("Пользователь username(" + loggedUser.getUsername()
                        + ") Передал показания " + typeMeterReading.getTitle()
                        + ": " + meterReading.getReading());
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new InputDataConflictException("Вы передавали показания по данному счетчику в этом месяце");
        }
    }

    private Optional<MeterReading> filterByTypeMeterReadings(List<MeterReading> meterReadings, TypeMeterReading type) {
        return meterReadings.stream()
                .filter(mr -> mr.getType().equals(type)
                        && mr.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .findAny();
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
        User loggedUser = userService.getLoggedUser();
        if (loggedUser == null) {
            throw new NotFoundException("Войдите в систему");
        }
        List<MeterReading> meterReadings = meterReadingRepository
                .findAllByUserId(loggedUser.getId());
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