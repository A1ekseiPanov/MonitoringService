package aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import util.LocalDateTimeFormatter;

import java.time.LocalDateTime;

/**
 * Аспект для логирования выполнения методов.
 */
@Aspect
@Slf4j
public class LoggableAspect {

    /**
     * Точка среза для любого метода.
     */
    @Pointcut("execution(* ru.panov..*(..))")
    public void anyMethod() {
    }

    /**
     * Аспект, выполняющий логирование выполнения методов.
     *
     * @param proceedingJoinPoint точка соединения выполнения метода
     * @return результат выполнения метода
     * @throws Throwable если возникла ошибка при выполнении метода
     */
    @Around("anyMethod()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        log.info("Execution of method: ({}) {} finished. Execution time is {} ms.",
                LocalDateTimeFormatter.formatter(LocalDateTime.now()),
                proceedingJoinPoint.getSignature(),
                end);
        return result;
    }
}