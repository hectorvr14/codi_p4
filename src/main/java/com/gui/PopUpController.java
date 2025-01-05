// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopUpController {

    @FXML
    private Label error_text;

    @FXML
    private Button accept_btn;

    @FXML
    void closeWindow(ActionEvent event)
    {
        Stage stage = (Stage) this.accept_btn.getScene().getWindow();
        stage.close();
    }

}
