package com.ethan.obdjava.views;

import com.ethan.obdjava.ELM327.ELM327Command;
import com.ethan.obdjava.ELM327.ELM327ResponseParser;
import com.ethan.obdjava.communication.Baudrate;
import com.ethan.obdjava.communication.SerialPortConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jssc.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MainController {
    private boolean inCalculation = false;
    private TimerTask task = null;

    @FXML
    private ComboBox portComboBox, baudrateComboBox;

    @FXML
    private Button connectButton, refreshComboBoxButton, calculateButton;

    @FXML
    private VBox container;

    @FXML
    private Label speedLabel, rpmLabel, airInTakeLabel, engineTempLabel, versionLabel, statusLabel, messageLabel;


    @FXML
    private void initialize() {
        versionLabel.setText(String.format("OS: %s JDK: %s", System.getProperty("os.name"), System.getProperty("java.version")));

        fillComboBoxs();
    }

    @FXML
    public void onClickConnect() {
        if (SerialPortConnection.getSerialPortConnected() == null) {
            final String portToConnect = (String) portComboBox.getValue();
            final Integer baudrate = (Integer) baudrateComboBox.getValue();
            final boolean response = SerialPortConnection.establishConnection(portToConnect, baudrate);

            if (response) {
                connect();
            } else {
                statusLabel.setText("Unable to establish a connection");
            }
        } else {
            final boolean response = SerialPortConnection.closeConnection();
            if (response) {
                disconnect();
            }
        }
    }

    @FXML
    public void onRefreshComboBox() {
        fillComboBoxs();
    }

    @FXML
    public void onClickRefresh() {
        if (!inCalculation) {
            readBasicData();
        }
    }

    @FXML
    public void onClickClearDTC() {
        final boolean response = writeDate(ELM327Command.CLEARDTC, "44");
        if (response) {
            showMessage("DTC Cleared");
        } else {
            showMessage("DTC not cleared");
        }
    }

    // Huge function we need to do something better with task management for the future
    @FXML
    public void onClickCalculate0To100() {
        if (!inCalculation) {
            Integer speed = readData(ELM327Command.SPEED);
            speedLabel.setText("Speed : " +  speed + " km/h");

            if (speed == 0) {
                inCalculation = true;
                calculateButton.setText("Cancel");
                showMessage("Ready? GO!");

                new Timer().schedule(task = new TimerTask() {
                    LocalDateTime startDate = null;
                     public void run() {
                         Integer currentSpeed = readData(ELM327Command.SPEED);
                         Platform.runLater(() -> speedLabel.setText("Speed : " +  currentSpeed + " km/h"));
                         if (currentSpeed >= 100) {
                             String time = MainViewUtils.compareTwoDateTime(startDate, LocalDateTime.now());
                             Platform.runLater(() -> {
                                 showMessage("0-100 : " + time);
                                 stop0To100Task();
                             });
                        } else if (startDate == null && currentSpeed > 0) {
                             Platform.runLater(() -> showMessage("In recording..."));
                             startDate = LocalDateTime.now();
                         }
                     }
                }, 0, 200);
            } else {
                showMessage("You must be at 0 km/h, slow down !");
            }
        } else if (task != null) {
            stop0To100Task();
            clearMessage();
        }
    }

    private void stop0To100Task() {
        task.cancel();
        task = null;
        inCalculation = false;
        calculateButton.setText("Calculate 0-100 km/h");
    }

    private void fillComboBoxs() {
        ObservableList<String> portNames = FXCollections.observableArrayList(SerialPortList.getPortNames());
        portComboBox.setItems(portNames);

        ObservableList<Integer> baudrates = FXCollections.observableArrayList(Arrays.stream(Baudrate.values())
                .map(Baudrate::getBaudrate)
                .collect(Collectors.toList()));
        baudrateComboBox.setItems(baudrates);
    }

    private void connect() {
        portComboBox.setVisible(false);
        refreshComboBoxButton.setVisible(false);
        baudrateComboBox.setVisible(false);
        connectButton.setText("Disconnect");
        statusLabel.setText("Connected");
        container.setVisible(true);
        clearMessage();

        readBasicData();
    }

    private void disconnect() {
        portComboBox.setVisible(true);
        refreshComboBoxButton.setVisible(true);
        baudrateComboBox.setVisible(true);
        connectButton.setText("Establish Connection");
        statusLabel.setText("Disconnected");
        container.setVisible(false);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    private void clearMessage() {
        messageLabel.setText("");
        messageLabel.setVisible(false);
    }

    private void readBasicData() {
        speedLabel.setText("Speed : " +  readData(ELM327Command.SPEED) + " km/h");
        rpmLabel.setText("RPM : " + readData(ELM327Command.RPM) + " tr/min");
        airInTakeLabel.setText("AirInTake : " + readData(ELM327Command.AIRINTAKE) + " g/s");
        engineTempLabel.setText("Engine Temp : " + readData(ELM327Command.ENGINETEMP) + " °C");
        clearMessage();
    }

    private boolean writeDate(ELM327Command command, String validCode) {
        try {
            SerialPortConnection.getSerialPortConnected().writeString(command.getCode() + "\r\n");
            Thread.sleep(1000);
            String responseHex = SerialPortConnection.getSerialPortConnected().readString();
            if (responseHex.contains(validCode)) {
                return true;
            }
            return false;
        } catch (SerialPortException | InterruptedException e) {
            disconnect();
            return false;
        }
    }

    private Integer readData(ELM327Command command) {
        try {
            SerialPortConnection.getSerialPortConnected().writeString(command.getCode() + "\r\n");
            Thread.sleep(100);
            return ELM327ResponseParser.parseCommandData(command, SerialPortConnection.getSerialPortConnected().readString());
        } catch (SerialPortException | InterruptedException e) {
            disconnect();
            return 0;
        }
    }
}