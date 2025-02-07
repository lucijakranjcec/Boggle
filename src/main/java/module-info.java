module hr.tvz.boggle.boggleapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires lombok;

    opens hr.tvz.boggle.boggleapplication to javafx.fxml;
    exports hr.tvz.boggle.boggleapplication;
    exports hr.tvz.boggle.util;
    opens hr.tvz.boggle.util to javafx.fxml;
}