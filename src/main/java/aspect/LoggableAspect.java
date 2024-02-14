package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Аспект для логирования выполнения методов.
 */
@Aspect
public class LoggableAspect {

    /**
     * Точка среза для любого метода.
     */
    @Pointcut("execution(* * (..))")
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
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + end + " ms.");
        return result;
    }
}