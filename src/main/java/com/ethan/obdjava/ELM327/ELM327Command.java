package com.ethan.obdjava.ELM327;

public enum ELM327Command {
    SPEED("010D"), RPM("010C"), AIRINTAKE("0110"), ENGINETEMP("0105"), CLEARDTC("04");

    private final String code;

    ELM327Command(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
