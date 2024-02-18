package ru.panov.validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    String message;

    @Override
    public String toString() {
        return message;
    }
}