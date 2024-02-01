package exception;


/**
 * Исключение, выбрасываемое при конфликте данных.
 * Это исключение возникает, когда данные, введенные пользователем,
 * конфликтуют с уже существующими данными или ожидаемыми условиями.
 */
public class InputDataConflictException extends RuntimeException {
    public InputDataConflictException(String message) {
        super(message);
    }
}