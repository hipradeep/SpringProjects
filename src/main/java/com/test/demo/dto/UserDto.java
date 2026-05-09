package com.test.demo.dto;

import com.test.demo.annotation.ValidPan;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    @NotBlank(message = "Name is required")
    private String name;

    @ValidPan
    private String panCard;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }
}
