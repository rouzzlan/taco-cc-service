package com.falcontech.model;

public record CreditCard(
    String number,
    String expiration,
    Integer cvv,
    String owner,
    String hash) {}
