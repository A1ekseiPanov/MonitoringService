package service;

import lombok.AllArgsConstructor;
import model.Audit;
import repository.AuditRepository;

import java.util.List;

/**
 * Реализация сервиса журналирования.
 */

@AllArgsConstructor
public class AuditServiceImpl implements AuditService {
    public final AuditRepository repository;

    /**
     * Создает запись.
     *
     * @param audit объект аудита для сохранения
     */
    @Override
    public void create(Audit audit) {
        repository.save(audit);
    }

    /**
     * Возвращает список всех записей.
     *
     * @return список всех записей аудита
     */
    @Override
    public List<Audit> getAll() {
        return repository.findAll();
    }
}