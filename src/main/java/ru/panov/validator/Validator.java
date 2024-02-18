package ru.panov.validator;

public interface Validator<T> {
     ValidatorResult isValid(T object);
}