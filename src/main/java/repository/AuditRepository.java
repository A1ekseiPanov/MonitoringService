package repository;

import entity.Audit;

public interface AuditRepository {
    Audit save(Audit audit);
}
