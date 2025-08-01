package ru.practicum.shareit.exception;

public class ErrorArgumentException extends IllegalArgumentException {

    public ErrorArgumentException(String message) {
        super(message);
    }
}