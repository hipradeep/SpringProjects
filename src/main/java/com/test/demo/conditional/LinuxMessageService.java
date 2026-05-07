package com.test.demo.conditional;

public class LinuxMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "Service running on Linux OS";
    }
}
