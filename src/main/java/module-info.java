module com.example.tictactoesocketfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tictactoesocketfx to javafx.fxml;
    exports com.example.tictactoesocketfx;
}