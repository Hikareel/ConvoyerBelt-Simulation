package com.example.projektpw;

import java.util.LinkedList;

public class Truck {
    public int container;
    public LinkedList<Brick> bricksOnBelt = new LinkedList<Brick>();

    public Truck(int container) {
        this.container = container;
    }
}
