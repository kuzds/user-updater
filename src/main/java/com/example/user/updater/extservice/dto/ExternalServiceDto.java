package com.example.user.updater.extservice.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ExternalServiceDto {

    private String login;
    private String fullName;
    private String phoneNumber;
    private String email;
    private OffsetDateTime birthDay;
}