package com.example.projektpiwonska;

import javafx.application.Platform;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.Random;

    //konstruktor
public class Bufor {
        public volatile int licz_uslug;
        public volatile int poj_poczek;
        public volatile int licz_foteli;
        public volatile  Semaphore mutex;
        public volatile Semaphore klient;
        public volatile Semaphore[] fryzjer;
        public volatile Semaphore pracownik;
        public volatile ArrayBlockingQueue<Integer> service;
        public volatile ArrayBlockingQueue<Integer> id_klient;
        public volatile Semaphore fotel;
        public volatile int l_czek;
        public int loops;
        public volatile int licz_klientow;
        public volatile int k;
        public volatile int zajete_fotele = 0;
        Random rand = new Random();
        public volatile int[] licz_fryz_tej_uslugi;
        public volatile int obsluzeni;
        Configuration configuration;



        //konstruktor
        public Bufor(int licz_uslug, int poj_poczek, int licz_foteli, int licz_klientow, int loops, int licz_fryzjerow, Configuration configuration){
            this.licz_uslug = licz_uslug;
            this.poj_poczek = poj_poczek;
            this.licz_foteli = licz_foteli;
            this.k = licz_klientow;
            this.obsluzeni = 0;
            this.mutex = new Semaphore(1);
            this.klient = new Semaphore(0);
            this.pracownik = new Semaphore(licz_fryzjerow);
            this.fryzjer = new Semaphore[licz_uslug];
            this.fotel = new Semaphore(licz_foteli);
            this.licz_klientow = licz_klientow;
            this.service = new ArrayBlockingQueue<Integer>(licz_klientow);
            this.loops = loops;
            this.id_klient = new ArrayBlockingQueue<Integer>(licz_klientow);
            arr=new Data[licz_foteli];
            this.licz_fryz_tej_uslugi=new int[licz_uslug];
            this.configuration=configuration;
            for(int i=0; i<licz_uslug; i++){
                licz_fryz_tej_uslugi[i] = rand.nextInt(licz_fryzjerow)+1;
                configuration.spisUslugLabel[i].setText("Usluga "+configuration.uslugiFryzjerow.get(i)+" liczba Fryzjerow: "+licz_fryz_tej_uslugi[i]);
                fryzjer[i] = new Semaphore(licz_fryz_tej_uslugi[i]);
            }

        }
        public Data[] arr;
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
