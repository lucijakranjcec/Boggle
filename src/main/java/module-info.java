module hr.tvz.boggle.boggleapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens hr.tvz.boggle.boggleapplication to javafx.fxml;
    exports hr.tvz.boggle.boggleapplication;
}