package com.ethan.obdjava.communication;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialPortConnection {
    private static SerialPort serialPortConnected = null;

    public static boolean establishConnection(String portToConnect, Integer baudrate) {
        if (portToConnect == null || portToConnect.isEmpty() || baudrate == null) {
            return false;
        }
        try {
            SerialPort serialPort = new SerialPort(portToConnect);
            serialPort.openPort();
            serialPort.setParams(baudrate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.writeString("ATZ\r\n");
            Thread.sleep(500);
            serialPort.writeString("ATSP0\r\n");
            Thread.sleep(500);

            String response = serialPort.readString();
            if (!response.contains("OK")) {
                return false;
            }
            serialPortConnected = serialPort;

            return true;
        } catch (SerialPortException | InterruptedException e) {
            return false;
        }
    }

    public static boolean closeConnection() {
        try {
            serialPortConnected.closePort();
            serialPortConnected = null;
            return true;
        } catch (SerialPortException e) {
            return false;
        }
    }

    public static SerialPort getSerialPortConnected() {
        return serialPortConnected;
    }
}
