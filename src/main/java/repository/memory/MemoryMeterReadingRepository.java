package repository.memory;

import entity.MeterReading;
import entity.Role;
import entity.User;
import exception.NotFoundException;
import repository.MeterReadingRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class MemoryMeterReadingRepository implements MeterReadingRepository {
    private Long id = 0L;
    private static MemoryMeterReadingRepository INSTANCE = new MemoryMeterReadingRepository();
    private final UserRepository userRepository = MemoryUserRepository.getInstance();
    private List<MeterReading> meterReadingList = new ArrayList<>();

    private MemoryMeterReadingRepository() {
    }

    public static void resetInstance() {
        INSTANCE = new MemoryMeterReadingRepository();
    }

    public static MemoryMeterReadingRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает список всех показаний счетчиков для пользователя с указанным идентификатором.
     *
     * @param userId Идентификатор пользователя
     * @return Список всех показаний счетчиков для конкретного пользователя или всех показаний для админа
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public List<MeterReading> findAllMeterReadingByUserId(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id:%s не найден", userId));
        }
        if (user.getRole().equals(Role.ADMIN.toString())) {
            return meterReadingList;
        } else {
            return user.getMeterReadings();
        }
    }

    /**
     * Сохраняет показание счетчика для пользователя с указанным идентификатором.
     *
     * @param meterReading Показание счетчика для сохранения
     * @param userId       Идентификатор пользователя
     * @return Сохраненное показание счетчика
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public MeterReading save(MeterReading meterReading, Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id:%s не найден", userId));
        }
        List<MeterReading> meterReadings = user.getMeterReadings();
        if (meterReading.getId() == null) {
            meterReading.setId(++id);
        }
        meterReadings.add(meterReading);
        meterReading.setUser(user);
        user.setMeterReadings(meterReadings);
        meterReadingList.add(meterReading);
        userRepository.save(user);
        return meterReading;
    }
}