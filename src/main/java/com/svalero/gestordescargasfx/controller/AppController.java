package com.svalero.gestordescargasfx.controller;

import com.svalero.gestordescargasfx.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    public TextField tfUrl;
    public Button btDownload;
    public TabPane tpDownloads;

    private Map<String, DownloadController> allDownloads;

    public AppController() {
        allDownloads = new HashMap<>();
    }

    @FXML
    public void launchDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        launch(urlText);
    }


    public void launchFileDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();

    }

    @FXML
    public void openLog(ActionEvent actionEvent) throws IOException, IllegalArgumentException {
        try {
            File file = new File("GestorDescargas.log");
            Desktop.getDesktop().open(file);
        } catch (IOException ioe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ha habido un error.");
            alert.show();
        } catch (IllegalArgumentException iae) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found.");
            alert.show();
        }

    }

    private void launch(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("download.fxml"));

            DownloadController downloadController = new DownloadController(url);
            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, downloadBox));

            allDownloads.put(url, downloadController);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }
}