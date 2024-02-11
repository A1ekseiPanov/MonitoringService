package aspect;

import entity.Audit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import repository.jdbc.JdbcAuditRepository;

/**
 * Аспект для аудита выполнения методов, помеченных аннотацией @Audit.
 */
@Aspect
public class AuditAspect {
    private final JdbcAuditRepository repository = new JdbcAuditRepository();

    /**
     * Точка среза для методов, помеченных аннотацией @Audit.
     */
    @Pointcut("within(@annotations.Audit *) && execution(* * (..))")
    public void annotatedByAudit() {
    }

    /**
     * Аспект, выполняющий аудит выполнения методов с сохранением в бд.
     *
     * @param proceedingJoinPoint точка соединения выполнения метода
     * @return результат выполнения метода
     */
    @Around("annotatedByAudit()")
    public Object audit(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            repository.save(new Audit("Method execution successful: "
                    + proceedingJoinPoint.getSignature().getDeclaringTypeName()));
        } catch (Throwable t) {
            repository.save(new Audit("Method execution failed: "
                    + proceedingJoinPoint.getSignature().getDeclaringTypeName()
                    + " " + t.getMessage()));
        }
        return result;
    }
}