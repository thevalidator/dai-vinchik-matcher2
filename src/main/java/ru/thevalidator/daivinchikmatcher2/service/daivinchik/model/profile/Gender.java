package ru.thevalidator.daivinchikmatcher2.service.daivinchik.model;

public enum Gender {

    FEMALE("1"),
    MALE("2"),
    ALL("3");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
