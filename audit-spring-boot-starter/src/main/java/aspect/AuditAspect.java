package aspect;

import lombok.AllArgsConstructor;
import model.Audit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import service.AuditService;


/**
 * Аспект для аудита выполнения методов, помеченных аннотацией @Audit.
 */
@Aspect
@AllArgsConstructor
public class AuditAspect {
    private final AuditService service;


    /**
     * Точка среза для методов, помеченных аннотацией @Audit.
     */
    @Pointcut("within(@annotations.Audit *) && execution(* * (..))")
    public void annotatedByAudit() {
    }

    /**
     * Логирование успешного выполнения метода контроллера.
     *
     * @param joinPoint точка соединения
     */
    @AfterReturning(pointcut = "annotatedByAudit()")
    public void logControllerMethodSuccess(JoinPoint joinPoint) {
        service.create(new Audit("Method executed successfully: "
                + joinPoint.getSignature()));
    }

    /**
     * Логирование исключения в методе контроллера.
     *
     * @param joinPoint точка соединения
     * @param ex        исключение
     */
    @AfterThrowing(pointcut = "annotatedByAudit()", throwing = "ex")
    public void logControllerMethodException(JoinPoint joinPoint, Throwable ex) {
        service.create(new Audit("Exception in method "
                + joinPoint.getSignature() + ": " + ex.getMessage()));
    }
}