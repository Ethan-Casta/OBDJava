package com.ethan.obdjava.communication;

public enum Baudrate {
    BAUDRATE_9600(9600), BAUDRATE_38400(38400);

    private final int baudrate;

    Baudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getBaudrate() {
        return baudrate;
    }
}
