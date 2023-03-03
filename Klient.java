package com.example.projektpiwonska;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
public class Klient extends Thread{
    volatile Bufor bufor;
    static int j;
    //indeks klienta
    public int K;
    //usługa, którą wybrał klient
    public int usluga;
    //pomocniczo
    public static int idK = 0;
    Random rand = new Random();
    Random randomTime = new Random();
    Configuration configuration;
    int id;
    static Color[] kolory=new Color[]{Color.BLUE, Color.BROWN, Color.GREEN, Color.YELLOW, Color.GOLD};

    public Klient (Bufor bufor, Configuration configuration, int id) {
        this.usluga = rand.nextInt(bufor.licz_uslug);
        this.bufor = bufor;
        K = idK; idK++;
        this.configuration=configuration;
        this.id=id;
    }
    public void run () {
        for(int i=0; i<bufor.loops; i++){
            bufor.obsluzeni++;
            //sprawy własne
            try {
                //obliczenie czasu tworzenia koła
                int dt = configuration.klientMin + randomTime.nextInt(configuration.klientMax + 1);
                Thread.sleep(dt/2);
                bufor.mutex.acquire();
                if(bufor.l_czek < bufor.poj_poczek){
                    bufor.l_czek = bufor.l_czek + 1;
                    //////////Blok animacji tworzenia koła/////////
                    //powołanie koła w wyznaczonym miejscu
                    Circle circle=new Circle();
                    circle.setCenterX(configuration.poczekalniaCircleCoords[0].x);
                    circle.setCenterY(configuration.poczekalniaCircleCoords[0].y);
                    circle.setRadius(10.0);
                    //przypisanie koloru
                    int whichColor=id% kolory.length;
                    circle.setFill(kolory[whichColor]);
                    circle.setStrokeWidth(20);
                    //tworzenie animacji tworzenia kola
                    ScaleTransition scaleTransition = new ScaleTransition();
                    scaleTransition.setDuration(Duration.millis(200));
                    scaleTransition.setNode(circle);
                    scaleTransition.setByY(1.5);
                    scaleTransition.setByX(1.5);
                    scaleTransition.setAutoReverse(false);
                    scaleTransition.setRate(configuration.lAnimationRate);
                    configuration.lAnimation.add(scaleTransition);
                    scaleTransition.setOnFinished(e -> {
                        bufor.unblock();
                        configuration.lAnimation.remove(scaleTransition);
                    });


                    //wywołanie animacji nie jawne
                    Platform.runLater(() -> {
                        //Main.animationPane.getChildren().add(circle);
                       scaleTransition.play();
                    });

                    Data dana = new Data(randomTime.nextInt(100), id, circle, K);
                    //bufor.block();
                    //dodanie koła na ekran
                    Platform.runLater(()->{
                        Main.animationPane.getChildren().add(circle);
                    });
                    ///Aktualizacja etykiety poczekalni
                    Platform.runLater(() -> {
                        configuration.poczekalniaLabel[0].setText("W poczekalni: " + bufor.l_czek);
                    });
                    System.out.println("^^^ Nowy klient K-" + K + " w poczekalni. Poczekalnia: " + bufor.l_czek + "/" + bufor.poj_poczek);
                    ///przypisanie miejsca w tablicy danych z której potem zostanie pobrane na fotel
                    int ii=j;
                    j=(j+1)% configuration.liczbaFoteli;
                    bufor.arr[ii] = dana;


                    bufor.klient.release();
                    bufor.mutex.release();
                    bufor.service.add(usluga);
                    bufor.id_klient.add(K);
                    bufor.pracownik.acquire();
                    bufor.fryzjer[usluga].acquire();

                    //STRZYZENIE
                }
                else {
                    //odchodzi z zakladu nieobsluzony
                    System.out.println("---- Klient K-" + K + " odchodzi z zakładu nieobsłużony");
                    bufor.licz_klientow--;
                    bufor.mutex.release();
                }
                Thread.sleep(dt/2);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}


