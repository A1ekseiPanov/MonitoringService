package ru.panov.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.panov.util.LocalDateTimeFormatter;

import java.time.LocalDateTime;

/**
 * Аспект для логирования выполнения методов.
 */
@Aspect
@Component
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
        System.out.println("Execution of method: (" + LocalDateTimeFormatter.formatter(LocalDateTime.now()) +
                           ") " + proceedingJoinPoint.getSignature() +
                           " finished. Execution time is " + end + " ms.");
        return result;
    }
}