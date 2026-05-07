package com.test.demo.conditional;

public class DefaultNotificationService implements NotificationService {
    @Override
    public String send(String message) {
        return "Default Notification: " + message;
    }
}
