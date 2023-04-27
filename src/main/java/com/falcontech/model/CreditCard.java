package com.falcontech.model;

public record CreditCard(
    Integer number,
    String expiration,
    Integer cvv,
    String owner,
    String hash) {}
