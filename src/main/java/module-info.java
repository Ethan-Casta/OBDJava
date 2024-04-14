module com.ethan.obdjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires jssc;

    opens com.ethan.obdjava to javafx.fxml;
    exports com.ethan.obdjava;
    exports com.ethan.obdjava.views;
    opens com.ethan.obdjava.views to javafx.fxml;
    exports com.ethan.obdjava.ELM327;
    opens com.ethan.obdjava.ELM327 to javafx.fxml;
}