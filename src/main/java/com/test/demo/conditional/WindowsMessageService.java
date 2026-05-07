package com.test.demo.conditional;

public class WindowsMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "Service running on Windows OS";
    }
}
