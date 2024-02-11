package service;

import annotations.Audit;
import exception.InputDataConflictException;
import exception.ValidationException;
import mapper.MeterReadingMapper;
import repository.MeterReadingRepository;
import entity.dto.MeterReadingDto;
import entity.MeterReading;
import entity.Role;
import entity.TypeMeterReading;
import entity.User;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import validator.MeterReadingValidator;
import validator.MonthYearValidator;
import validator.ValidatorResult;
import util.AuditLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Audit
public class MeterReadingService {
    private final UserService userService;
    private final MeterReadingRepository meterReadingRepository;
    private final TypeMeterReadingService typeMeterReadingService;
    private final MeterReadingValidator meterReadingValidator = MeterReadingValidator.getInstance();
    private final MonthYearValidator monthYearValidator = MonthYearValidator.getInstance();
    private final MeterReadingMapper mapper = MeterReadingMapper.getInstance();

    /**
     * Возвращает список всех показаний счетчиков для вошедшего в систему пользователя.
     *
     * @return Список всех показаний счетчиков для текущего пользователя
     * @throws NotFoundException если пользователь не вошел в систему или у него нет показаний
     */
    public List<MeterReading> getReadingHistory(Long userId) {
        User user = null;
        try {
        user = userService.getById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        List<MeterReading> userReadings = null;
        try {

            userReadings = Objects.equals(user.getRole(), Role.ADMIN.toString())
                    ? meterReadingRepository.findAll()
                    : meterReadingRepository.findAllByUserId(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
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
    public List<MeterReading> getLatestReadingsByTypes(Long userId) {
        return typeMeterReadingService.getAll().stream()
                .map(type -> getLatestReadingByType(type, userId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MeterReading getLatestReadingByType(TypeMeterReading type, Long userId) {
        return getReadingHistory(userId).stream()
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
    public void submitMeterReading(TypeMeterReading typeMeterReading, BigDecimal reading, Long userId) {
        MeterReadingDto meterReading = new MeterReadingDto(typeMeterReading.getId(), reading);
        ValidatorResult validatorResult = meterReadingValidator.isValid(meterReading);
        List<MeterReading> meterReadings = meterReadingRepository.findAllByUserId(userId);
        if (meterReadings.isEmpty() || filterByTypeMeterReadings(meterReadings, typeMeterReading).isEmpty()) {
            if (!validatorResult.isValid()) {
                throw new ValidationException(validatorResult.getErrors());
            }
            try {
                meterReadingRepository.save(mapper.meterReadingDtoToMeterReading(meterReading), userId);
                AuditLog.logAction("Пользователь id(" + userId
                        + ") Передал показания " + typeMeterReading.getTitle()
                        + ": " + meterReading.getReading());
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
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
    public List<MeterReading> getAllMeterReadingsByMonth(int month, int year, Long userId) {
        ValidatorResult valid = monthYearValidator.isValid(month, year);
        List<MeterReading> meterReadings = meterReadingRepository
                .findAllByUserId(userId);
        List<MeterReading> meterReading = null;
        if (valid.isValid()) {
            meterReading = meterReadings.stream()
                    .filter(mr -> mr.getLocalDate().getMonthValue() == month
                            && mr.getLocalDate().getYear() == year)
                    .toList();
        }else {
            throw new ValidationException(valid.getErrors());
        }
         if (meterReading.isEmpty()) {
            throw new NotFoundException("В данном месяце нет показаний");
        }
        return meterReading;
    }
}