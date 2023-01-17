package com.example.projektpw;

import java.util.LinkedList;

public class Truck {
    public int container;
    public int id = 1;
    public static int nextId = 1;
    public LinkedList<Brick> bricksOnBelt = new LinkedList<Brick>();

    public Truck(int container) {
        this.container = container;
        this.id = nextId;
        nextId++;
    }
}
