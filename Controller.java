package com.example.projektpiwonska;

import javafx.animation.Animation.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField fryzjerNumText;
    @FXML
    private TextField uslugiNumText;
    @FXML
    private TextField foteleNumText;
    @FXML
    private TextField poczekalniaNumText;
    @FXML
    private TextField klienciMinText;
    @FXML
    private TextField klienciMaxText;
    @FXML
    private TextField wykonanieMinText;
    @FXML
    private TextField wykonanieMaxText;
    @FXML
    private Button saveBtn;
    @FXML
    private Button startBtn;
    @FXML
    private Button stopBtn;
    @FXML
    private Button koniecBtn;
    @FXML
    private Slider fryzjerSpeedSlider;

    public Controller() {
    }

    //inicjalizacja początkowa
    @FXML
    public void initialize() {
        this.startBtn.setDisable(true);
        this.stopBtn.setDisable(true);
        this.stopBtn.setText("stop/resume");
        this.koniecBtn.setDisable(true);
        this.fryzjerSpeedSlider.setDisable(true);
    }
    //Dzialanie przycisku zapisu
    @FXML
    protected void onSaveButtonAction() {
        try {
            int screenWidth = (int)Main.animationPane.getWidth();
            int screenHeight = (int)Main.animationPane.getHeight();
            int fryzjerNum = 0;
            int uslugaNum = 0;
            int foteleNum = 0;
            int poczekalaniaNum = 0;

            //pobranie wartości z GUI i zapisanie do konfiguracji
            try {
                fryzjerNum = Integer.parseInt(this.fryzjerNumText.getText());
            } catch (NumberFormatException e) {
                this.fryzjerNumText.setText("Blad");
                throw new DataErrorException();
            }


            try {
                uslugaNum = Integer.parseInt(this.uslugiNumText.getText());
            } catch (NumberFormatException e) {
                this.uslugiNumText.setText("Blad");
                throw new DataErrorException();
            }


            try {
                foteleNum = Integer.parseInt(this.foteleNumText.getText());
            } catch (NumberFormatException e) {
                this.foteleNumText.setText("Blad");
                throw new DataErrorException();
            }
            try{
                poczekalaniaNum=Integer.parseInt(this.poczekalniaNumText.getText());
            }
            catch (NumberFormatException e)
            {
                this.poczekalniaNumText.setText("Blad");
                throw new DataErrorException();
            }

            Main.config = new Configuration(screenWidth, screenHeight, fryzjerNum, uslugaNum, foteleNum, poczekalaniaNum);

            try {
                Main.config.klientMin = Integer.parseInt(this.klienciMinText.getText());
            } catch (NumberFormatException e) {
                this.klienciMinText.setText("Blad");
                throw new DataErrorException();
            }

            try {
                Main.config.klientMax = Integer.parseInt(this.klienciMaxText.getText());
            } catch (NumberFormatException e) {
                this.klienciMaxText.setText("Blad");
                throw new DataErrorException();
            }

            try {
                Main.config.uslugaMin = Integer.parseInt(this.wykonanieMinText.getText());
            } catch (NumberFormatException e) {
                this.wykonanieMinText.setText("Blad");
                throw new DataErrorException();
            }

            try {
                Main.config.uslugaMax = Integer.parseInt(this.wykonanieMaxText.getText());
            } catch (NumberFormatException e) {
                this.wykonanieMaxText.setText("Blad");
                throw new DataErrorException();
            }
            //wywołanie przygotowania animacji
            Main.config.prepareAnimation();
            this.saveBtn.setDisable(true);
            this.startBtn.setDisable(false);
            this.fryzjerSpeedSlider.setDisable(false);
        } catch (DataErrorException e) {
        }

    }
    //działanie przycisku start
    @FXML
    protected void onStartButtonAction() {
        if (Main.config != null) {
            Main.animationStatus = Status.RUNNING;
            Main.config.startThreads();
            this.startBtn.setDisable(true);
            this.stopBtn.setDisable(false);
            this.stopBtn.setText("Stop");
            this.koniecBtn.setDisable(false);
        }

    }
    //działanie przycisku stop
    @FXML
    protected void onStopButtonAction() {
        if (Main.config != null) {
            if (Main.animationStatus == Status.PAUSED) {
                Main.animationStatus = Status.RUNNING;
                Main.config.resumeAnimation();
                this.stopBtn.setText("Stop");
                this.startBtn.setDisable(true);
                this.koniecBtn.setDisable(false);
            } else if (Main.animationStatus == Status.RUNNING) {
                Main.animationStatus = Status.PAUSED;
                Main.config.pauseAnimation();
                this.saveBtn.setDisable(true);
                this.startBtn.setDisable(true);
                this.stopBtn.setText("resume");
                this.koniecBtn.setDisable(true);
            }
        }

    }
    //działanie przycisku końca
    @FXML
    protected void onKoniecButtonAction() {
        if (Main.animationStatus == Status.RUNNING) {
            Main.animationStatus = Status.STOPPED;
            Main.config.interruptThreads();
            Main.animationPane.getChildren().clear();
            this.saveBtn.setDisable(false);
            this.startBtn.setDisable(true);
            this.stopBtn.setDisable(true);
            this.stopBtn.setText("Stop/Resume");
            this.koniecBtn.setDisable(true);
            this.fryzjerSpeedSlider.setValue(0.0);
        }

    }

    @FXML
    public void onFryzjerSliderChanged() {
        Main.config.fryzjerzyAnimationSpeedChanged(this.fryzjerSpeedSlider.getValue());
    }


}
