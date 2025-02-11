module hr.tvz.boggle.boggleapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires lombok;
    requires java.rmi;
    requires java.naming;
    requires java.xml;

    opens hr.tvz.boggle to javafx.fxml;
    exports hr.tvz.boggle;
    exports hr.tvz.boggle.util;
    opens hr.tvz.boggle.util to javafx.fxml;
    exports hr.tvz.boggle.core;
    opens hr.tvz.boggle.core to javafx.fxml;
    exports hr.tvz.boggle.network;
    opens hr.tvz.boggle.network to javafx.fxml;
    exports hr.tvz.boggle.ui;
    opens hr.tvz.boggle.ui to javafx.fxml;
    exports hr.tvz.boggle.chat to java.rmi;
}
