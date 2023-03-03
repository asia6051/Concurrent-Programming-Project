package com.example.projektpiwonska;

import javafx.scene.shape.Circle;

public class Data {
    public int val;
    public int prawyId;
    public Circle circle;
    public int klient;
    public Data(int val, int prawyId, Circle circle, int klient)
    {
        this.val=val;
        this.prawyId=prawyId;
        this.circle=circle;
        this.klient=klient;
    }
}
