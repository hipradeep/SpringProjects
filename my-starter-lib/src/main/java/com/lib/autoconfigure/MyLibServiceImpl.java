package com.lib.autoconfigure;

public class MyLibServiceImpl implements MyLibService {
    @Override
    public String getInfo() {
        return "Hello from the External JAR Library!";
    }
}
