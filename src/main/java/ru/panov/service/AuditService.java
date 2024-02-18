package ru.panov.service;

import ru.panov.domain.model.Audit;

import java.util.List;

/**
 * Интерфейс AuditService определяет методы для работы с журналированием.
 */
public interface AuditService {
    void create(Audit audit);

    List<Audit> getAll();
}