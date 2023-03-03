package com.example.projektpiwonska;

import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Configuration {
    public int liczbaFryzjerow;
    public int liczbaUslug;
    public int liczbaFoteli;
    public int liczbaWPoczekalni;
    public int bigSquereSize;
    public int circleRadius;
    public int squereSpace;
    public volatile int licznikObsluzonych=0;
    int klientMin;
    int klientMax;
    int uslugaMin;
    int uslugaMax;
    double pAnimationRate;
    double lAnimationRate;
    //powołanie tablic miejsc w którym pojawią się obiekty na ekranie
    public XYCoord[] poczekalniaSquereCoords;
    public XYCoord[] foteleSquereCoords;
    public XYCoord[] finalSquereCoords;
    public XYCoord[] poczekalniaCircleCoords;
    public XYCoord[] foteleCircleCoords;
    public XYCoord[] finalCircleCoords;
    Label[] poczekalniaLabel;
    Label[] finalLabels;
    Label[] uslugaLabel;
    Label[] spisUslugLabel;
    List<Animation> lAnimation;
    List<Animation> pAnimation;
    //konstrukto konfiguracji

    public Configuration(int screenWidth, int screenHeight, int liczbaFryzjerow, int liczbaUslug, int liczbaFoteli, int liczbaWPoczekalni) {
        this.liczbaFryzjerow=liczbaFryzjerow;
        this.liczbaUslug=liczbaUslug;
        this.liczbaFoteli=liczbaFoteli;
        this.liczbaWPoczekalni=liczbaWPoczekalni;
        this.bigSquereSize = 65;
        this.circleRadius = 50;
        this.squereSpace = 15;
        //powolanie miejsc kwadratów foteli i ich ilości
        this.foteleSquereCoords=new XYCoord[liczbaFoteli];
        this.foteleCircleCoords=new XYCoord[liczbaFoteli];
        int i;
        for(i=0;i<foteleSquereCoords.length;i++)
        {
            this.foteleSquereCoords[i]=new XYCoord();
            foteleSquereCoords[i].x = 600;
            foteleSquereCoords[i].y = 50+i*(bigSquereSize+squereSpace);
            foteleCircleCoords[i] = new XYCoord();
            foteleCircleCoords[i].x = foteleSquereCoords[i].x + bigSquereSize / 2;
            foteleCircleCoords[i].y = foteleSquereCoords[i].y + bigSquereSize / 2;
        }
        //powolłanie miejsc kwadratów w poczekalni i ich ilości
        this.poczekalniaSquereCoords=new XYCoord[1];
        this.poczekalniaCircleCoords=new XYCoord[1];
        for(i=0;i<this.poczekalniaSquereCoords.length;++i)
        {
            this.poczekalniaSquereCoords[i]=new XYCoord();
            this.poczekalniaSquereCoords[i].x=200;
            this.poczekalniaSquereCoords[i].y=foteleSquereCoords[foteleSquereCoords.length/2].y-10;
            this.poczekalniaCircleCoords[i]=new XYCoord();
            this.poczekalniaCircleCoords[i].x=this.poczekalniaSquereCoords[0].x+this.bigSquereSize/2;
            this.poczekalniaCircleCoords[i].y=this.poczekalniaSquereCoords[0].y+this.bigSquereSize/2;
        }
        //powołanie kwadratu końcowego dla wszystkich obiektów
        this.finalSquereCoords= new XYCoord[1];
        this.finalCircleCoords= new XYCoord[1];
        this.finalSquereCoords[0]=new XYCoord();
        this.finalSquereCoords[0].x=1000;
        this.finalSquereCoords[0].y=poczekalniaSquereCoords[0].y;
        this.finalCircleCoords[0]=new XYCoord();
        this.finalCircleCoords[0].x=this.finalSquereCoords[0].x+this.bigSquereSize/2;
        this.finalCircleCoords[0].y=this.finalSquereCoords[0].y+this.bigSquereSize/2;

    }

    Rectangle[] poczekalniaSquare;
    Rectangle[] foteleSquare;
    Rectangle[] finalSquere;

    //przygotowanie animacji
    public void prepareAnimation() {
        this.lAnimation = new ArrayList<Animation>();
        this.pAnimation= new ArrayList<Animation>();
        this.pAnimationRate = 1.0;
        this.lAnimationRate = 1.0;
        this.poczekalniaSquare = new Rectangle[1];
        this.poczekalniaLabel = new Label[1];
        //powolanie obiektów i wyświetlenie ich na ekranie
        int i;
        for(i = 0; i < this.poczekalniaSquereCoords.length; ++i) {
            this.poczekalniaSquare[i] = new Rectangle((double)this.poczekalniaSquereCoords[i].x, (double)this.poczekalniaSquereCoords[i].y, (double)this.bigSquereSize, (double)this.bigSquereSize);
            this.poczekalniaSquare[i].setFill(Color.LIGHTGRAY);
            this.poczekalniaSquare[i].setStroke(Color.BLACK);
            this.poczekalniaLabel[0] = new Label();
            this.poczekalniaLabel[0].setLayoutX((double)this.poczekalniaSquereCoords[0].x);
            this.poczekalniaLabel[0].setLayoutY((double)(this.poczekalniaSquereCoords[0].y - 16));
            Main.animationPane.getChildren().addAll(this.poczekalniaSquare[i], poczekalniaLabel[i]);//, this.pLabels[0], this.pFinal[i]});
        }

        this.foteleSquare = new Rectangle[liczbaFoteli];
        this.uslugaLabel = new Label[liczbaFoteli];

        for(i = 0; i < this.foteleSquereCoords.length; ++i) {
            this.foteleSquare[i] = new Rectangle((double)this.foteleSquereCoords[i].x, (double)this.foteleSquereCoords[i].y, (double)this.bigSquereSize, (double)this.bigSquereSize);
            this.foteleSquare[i].setFill(Color.LIGHTGRAY);
            this.foteleSquare[i].setStroke(Color.BLACK);
            this.uslugaLabel[i] = new Label();
            this.uslugaLabel[i].setLayoutX((double)this.foteleSquereCoords[i].x);
            this.uslugaLabel[i].setLayoutY((double)(this.foteleSquereCoords[i].y - 16));
            Main.animationPane.getChildren().addAll(this.foteleSquare[i], this.uslugaLabel[i]);
        }
        this.finalSquere=new Rectangle[1];
        this.finalSquere[0]=new Rectangle((double)this.finalSquereCoords[0].x, (double)this.finalSquereCoords[0].y, (double)this.bigSquereSize, (double)this.bigSquereSize);
        this.finalSquere[0].setFill(Color.LIGHTGRAY);
        this.finalSquere[0].setStroke(Color.BLACK);
        this.finalLabels=new Label[1];
        this.finalLabels[0]=new Label();
        this.finalLabels[0].setLayoutX((double)this.finalSquereCoords[0].x);
        this.finalLabels[0].setLayoutY((double)this.finalSquereCoords[0].y-16);
        this.spisUslugLabel=new Label[liczbaUslug];
        for(i=0;i<liczbaUslug;i++)
        {
            this.spisUslugLabel[i]=new Label();
            this.spisUslugLabel[i].setLayoutX(100);
            this.spisUslugLabel[i].setLayoutY(this.foteleSquereCoords[0].y+i*16);
            Main.animationPane.getChildren().add(this.spisUslugLabel[i]);
        }
        Main.animationPane.getChildren().addAll(this.finalSquere[0], this.finalLabels[0]);
    }

    //Zmiana szybkości animacji
    public void fryzjerzyAnimationSpeedChanged(double sliderValue) {
        synchronized (lAnimation) {
            if (sliderValue >= 0) {
                lAnimationRate = 0.04* sliderValue + 1;
                pAnimationRate = 0.04* sliderValue + 1;
            } else {
                lAnimationRate = 4.5 / 500 * sliderValue + 1;
                pAnimationRate = 4.5 / 500 * sliderValue + 1;
            }
            for (Animation a : lAnimation) {
                a.setRate(lAnimationRate);
            }
            for (Animation a : pAnimation)
            {
                a.setRate(pAnimationRate);
            }
        }
    }
    //wznowienie animacji
    public void resumeAnimation() {
        synchronized (lAnimation) {
            for (Animation a : lAnimation) {
                a.play();
            }
        }
        synchronized (pAnimation) {
            for (Animation a : pAnimation) {
                a.play();
            }
        }
    }
    //wstrzymanie animacji
    public void pauseAnimation() {
        synchronized (lAnimation) {
            for (Animation a : lAnimation) {
                a.pause();
            }
        }
        synchronized (pAnimation) {
            for (Animation a : pAnimation) {
                a.pause();
            }
        }
    }


    //liczba klientow
    int liczba_klientow = 10;
    //liczba powtorzen
    int loops = 1000;

    Thread[] klienciTab;
    Thread[] fryzjerzyTab;
    List<String> uslugiFryzjerow = Arrays.asList("strzyżenie meskie", "golenie", "farbowanie", "Modelowanie", "strzyżenie damskie", "strzyżenie dziecięce", "dekoloryzacja", "upięcia", "pielenacja");

    public void startThreads() {
        Bufor bufor = new Bufor(liczbaUslug,liczbaWPoczekalni,liczbaFoteli,liczba_klientow, loops, liczbaFryzjerow, this);
        //powołanie wątków klientów i fryzjerów
        klienciTab=new Thread[liczba_klientow];
        fryzjerzyTab=new Thread[liczbaFryzjerow];
        Fryzjer.i=0;
        for(int i = 0; i < fryzjerzyTab.length; ++i){
            fryzjerzyTab[i] = new Thread(new Fryzjer(bufor, this), "F-"+i);
            fryzjerzyTab[i].start();
        }
        Klient.j=0;
        for(int i = 0; i < klienciTab.length; ++i){
            klienciTab[i] = new Thread(new Klient(bufor, this, i), "K-1"+i);
            klienciTab[i].start();
        }



    }

    public void interruptThreads() {
        for(int i = 0; i < fryzjerzyTab.length; i++){
                fryzjerzyTab[i].interrupt();

        }

        for(int i = 0; i < klienciTab.length; i++){

                klienciTab[i].interrupt();
        }
        System.out.println("Koniec dzialania programu\n");

    }
}
