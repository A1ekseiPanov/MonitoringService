package ru.panov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import model.Audit;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.AuditService;

import java.util.List;

import static ru.panov.controller.AuditController.AUDIT_PATH;

/**
 * Контроллер работы с журналом аудита.
 */
@RestController
@RequestMapping(AUDIT_PATH)
@AllArgsConstructor
@Tag(name = "Audit Controller", description = "Контроллер работы с журналом аудита")
public class AuditController {
    private final AuditService auditService;
    public static final String AUDIT_PATH = "/audit";

    /**
     * Получить все записи журнала аудита.
     *
     * @return Ответ с данными всех записей журнала аудита
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Все записи журнала аудита. Требуемая роль: Admin")
    public ResponseEntity<List<Audit>> getAllAudits() {
        return ResponseEntity.ok(auditService.getAll());
    }
}