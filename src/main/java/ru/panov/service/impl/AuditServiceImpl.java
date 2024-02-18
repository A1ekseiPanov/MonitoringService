package ru.panov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panov.domain.model.Audit;
import ru.panov.repository.AuditRepository;
import ru.panov.service.AuditService;

import java.util.List;

/**
 * Реализация сервиса журналирования.
 */
@Service
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