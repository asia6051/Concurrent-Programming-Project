package com.example.projektpiwonska;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.BlockingQueue;
import java.util.Random;

public class Fryzjer extends Thread{
    //indeks fryzjera
    public static int idF = 0;
    private int F;
    static int i;
    volatile Bufor bufor;
    //pomocniczo

    public volatile int licznikUslug=0;
    Random randomTime = new Random();
    Configuration configuration;
    //lista uslug wykonywanych przez fryzjerów
    List<String> uslugiFryzjerow = Arrays.asList("strzyżenie meskie", "golenie", "farbowanie", "Modelowanie", "strzyżenie damskie", "strzyżenie dziecięce", "dekoloryzacja", "upięcia", "pielenacja");


    //konstruktor
    public Fryzjer(Bufor bufor, Configuration configuration) {
        F = idF; idF++;
        this.bufor = bufor;
        this.configuration=configuration;

    }

    public void run () {
        final int a=0;



        while(bufor.obsluzeni < bufor.k * bufor.loops || bufor.licz_klientow > 0 || bufor.l_czek > 0)
        {
            int service, id_klient;
            try {
                bufor.klient.acquire();
                bufor.licz_klientow = Math.max(bufor.licz_klientow - 1, 0);
                //przypisanie fotela dla klienta z buforu
                int ii = i;
                i = (i + 1) % configuration.liczbaFoteli;

                bufor.fotel.acquire();

                //pobranie danych klienta z buforu
                Data data = bufor.arr[ii];
                Circle circle = data.circle;
                /////////////POoczątek Bloku Animacyjnego///////////////
                //powołanie i ustawienie animacji przejścia
                Path path = new Path();
                MoveTo moveTo = new MoveTo();
                moveTo.setX(configuration.poczekalniaCircleCoords[0].x);
                moveTo.setY(configuration.poczekalniaCircleCoords[0].y);
                LineTo lineTo = new LineTo();
                lineTo.setX(configuration.foteleCircleCoords[ii].x);
                lineTo.setY(configuration.foteleCircleCoords[ii].y);
                path.getElements().addAll(moveTo, lineTo);
                //czas wejścia na Fotel
                int czasWejsciaNaFotel = 200;
                PathTransition pathTransition = new PathTransition(Duration.millis(czasWejsciaNaFotel), path, circle);
                pathTransition.setRate(configuration.lAnimationRate);
                configuration.lAnimation.add(pathTransition);
                pathTransition.setOnFinished(e -> {
                    unblock();
                    configuration.lAnimation.remove(pathTransition);
                });
                //wykoananie animacji
                Platform.runLater(() -> {
                    Main.animationPane.getChildren().add(path);
                    pathTransition.play();
                });
                block();
                Platform.runLater(() -> {
                    Main.animationPane.getChildren().remove(path);
                });
                ////////////Koniec animacji wejścia na Fotel////////////

                bufor.zajete_fotele++;


                System.out.println(">>>> Klient K-" + /*data.klient +*/  " zajął fotel. Zajęte fotele: " + bufor.zajete_fotele + "/" + bufor.licz_foteli);

                bufor.mutex.acquire();
                bufor.l_czek = bufor.l_czek - 1;
                System.out.println(bufor.l_czek);
                //Wyswietlenie ile osób aktualnie jest w poczekalni
                Platform.runLater(() -> {
                    configuration.poczekalniaLabel[0].setText("W poczekalni: " + bufor.l_czek);
                });
                service = bufor.service.take();
                id_klient = bufor.id_klient.take();
                bufor.fryzjer[service].release();
                bufor.pracownik.release();
                bufor.mutex.release();
                
                //wyświetlenie jaka aktualnie jest wykonywana usługa
                Platform.runLater(() -> {
                    configuration.uslugaLabel[ii].setText("Wykonywana usluga: " + uslugiFryzjerow.get(service));
                });


                //obliczenie czasu wykonywania usługi i uśpienie na ten czas
                int czasWykonaniaUslugi = configuration.uslugaMin + randomTime.nextInt(configuration.uslugaMax + 1);
                Thread.sleep((czasWykonaniaUslugi));



                System.out.println("++++ Fryzjer F-" + F + " wykonuje usługę " + uslugiFryzjerow.get(service) + " klientowi K-" + id_klient);


                Platform.runLater(() -> {
                    configuration.uslugaLabel[ii].setText("Wykonywana Usluga: brak");
                });
                bufor.zajete_fotele--;
                //////////////Blok animacyjny zejścia z Fotela//////////
                Path path2 = new Path();
                MoveTo moveTo2 = new MoveTo();
                moveTo2.setX(configuration.foteleCircleCoords[ii].x);
                moveTo2.setY(configuration.foteleCircleCoords[ii].y);
                LineTo lineTo2 = new LineTo();
                lineTo2.setX(configuration.finalCircleCoords[0].x);
                lineTo2.setY(configuration.finalCircleCoords[0].y);
                path2.getElements().addAll(moveTo2, lineTo2);
                int czasZejsciaZFotela = 200;
                PathTransition pathTransition2 = new PathTransition(Duration.millis(czasZejsciaZFotela), path2, circle);
                configuration.pAnimation.add(pathTransition2);
                pathTransition2.setRate(configuration.pAnimationRate);
                //wykoananie animacji
                pathTransition2.setOnFinished(e -> {
                    unblock();
                    configuration.pAnimation.remove(pathTransition2);
                });
                ///Wykonanie animacji zejścia
                Platform.runLater(() -> {
                    Main.animationPane.getChildren().add(path2);

                    pathTransition2.play();
                });
                block();
                Platform.runLater(() -> {
                    Main.animationPane.getChildren().remove(path2);
                });
                /////////Koniec Bloku animacji zejścia//////////

                int dt = 200;
                /////////przygotowanie animacji znikniecia kola///////
                ScaleTransition scaleTransition = new ScaleTransition();
                scaleTransition.setDuration(Duration.millis(dt));
                scaleTransition.setNode(circle);
                scaleTransition.setByY(-1.5);
                scaleTransition.setByX(-1.5);
                scaleTransition.setCycleCount(1);
                scaleTransition.setAutoReverse(false);
                configuration.pAnimation.add(scaleTransition);
                scaleTransition.setRate(configuration.pAnimationRate);
                scaleTransition.setOnFinished(e -> {
                    unblock();

                    configuration.pAnimation.remove(scaleTransition);
                });
                //wykonanie animacji znikania
                Platform.runLater(() -> {
                    scaleTransition.play();
                });
                block();
                Platform.runLater(() -> {
                    Main.animationPane.getChildren().remove(circle);
                });

                configuration.licznikObsluzonych++;
                Platform.runLater(() -> {
                    configuration.finalLabels[0].setText("Z usługi skorzystało: " + configuration.licznikObsluzonych);
                });
                System.out.println("<<<< Obsłużony klient K-" + id_klient + " wyszedł. Zajęte fotele: " + bufor.zajete_fotele + "/" + bufor.licz_foteli);
                bufor.fotel.release();




            } catch (InterruptedException e) {
                return;
            }
        }
    }
    ///Semafory pilnujące dokończenia animacji
    Semaphore bin=new Semaphore(0);
    public void block() throws InterruptedException
    {
        bin.acquire();
    }
    public void unblock()
    {
        bin.release();
    }

}