package repository;

import model.Audit;

import java.util.List;

/**
 * Интерфейс AuditRepository определяет методы для работы с журналированием.
 */
public interface AuditRepository {
    Audit save(Audit audit);

    List<Audit> findAll();
}