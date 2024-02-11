package validator;

public interface Validator<T> {
     ValidatorResult isValid(T object);
}