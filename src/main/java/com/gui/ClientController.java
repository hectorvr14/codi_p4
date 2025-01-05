// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    // Interfaz remota do servidor
    private CallbackServerInterface server;

    // Interfaz remota do cliente
    private CallbackClientImpl client;

    // Tempo de subscrición restante
    private int tempo;

    // Número de datos recibidos por este cliente
    private int dataCount;

    @FXML
    private Button subButton;

    @FXML
    private Button unsubButton;

    @FXML
    private TextField subTime;

    @FXML
    private Label remainingTime;

    @FXML
    private Label newSub;

    @FXML
    private LineChart<Number, Number> data;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private XYChart.Series series;

    @FXML
    public void subOnClick(ActionEvent event) throws IOException {
        // Compróbase que o campo de tempo ten un valor correcto (un enteiro), en caso contrario, popup de error
        if(this.subTime.getText().isBlank() || !this.subTime.getText().matches("[1-9]([0-9])*")){
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Erro");

            Parent root;
            Scene popup_scene;

            root = FXMLLoader.load(getClass().getResource("popup-error.fxml"));
            popup_scene = new Scene(root, 479, 129);

            popup.setScene(popup_scene);
            popup.showAndWait();
        }
        else {
            this.tempo = Integer.parseInt(this.subTime.getText());
            this.remainingTime.setVisible(true);
            this.remainingTime.setText("Tempo restante: " + this.tempo + " s");
            this.server.subscribe(this.client, tempo);
        }

        // Baléirase o campo
        this.subTime.setText("");
    }

    @FXML
    public void unSubOnClick(ActionEvent event) throws RemoteException {
        // Cancélase a subscrición
        this.server.unsubscribe(this.client);
        this.remainingTime.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Eixos
        final NumberAxis horizontal = new NumberAxis();
        final NumberAxis vertical = new NumberAxis();

        this.xAxis = horizontal;
        this.yAxis = vertical;

        // Inicialízase a serie de datos
        this.series = new XYChart.Series();
        this.series.setName("Intervalo RR");

        // Tempo cero
        this.tempo = 0;

        // Cero datos recibidos
        this.dataCount = 0;

        // De momento, o contador de tempo en pantalla permanece oculto
        this.remainingTime.setVisible(false);

        // URL do rexistro RMI do servidor
        String registryURL = "rmi://localhost:12345/callbackServer";

        try {
            this.client = new CallbackClientImpl();
            this.client.setController(this);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }

        try {
            // Busca do obxecto remoto
            this.server = (CallbackServerInterface) Naming.lookup(registryURL);
            System.out.println("Busca completada");
            System.out.println("O servidor saúda: " + this.server.sayHello());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void newData(double dato) {

        // Aumentamos a conta
        this.dataCount++;

        // Diminúese o tempo nunha unidade
        this.tempo--;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Elimínanse datos en caso de alcanzar a máxima capacidade
                if(series.getData().size() == 60) { series.getData().remove(0);}

                // Se se rematase a subscrición, ocúltase o contador - noutro caso, actualización
                if(tempo == 0) {
                    remainingTime.setVisible(false);
                }
                else { remainingTime.setText("Tempo restante: " + tempo + " s"); }

                // Engádese o dato á serie
                series.getData().add(new XYChart.Data(dataCount, dato));
                data.setData(FXCollections.observableArrayList(series));
            }
        });

    }

}