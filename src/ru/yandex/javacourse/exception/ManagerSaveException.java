package ru.yandex.javacourse.exception;

import java.io.File;
import java.io.IOException;

public class ManagerSaveException extends IOException {
    private final File file;

    public ManagerSaveException(final String message, final File file) {
        super(message);
        this.file = file;
    }

    public String getDetailMessage() {
        return getMessage() + " - " + file.getName();
    }
}
