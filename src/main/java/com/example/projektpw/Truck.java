package com.example.projektpw;

import javafx.application.Platform;

import java.util.LinkedList;

public class Truck {
    public int container;
    public int id = 1;
    public static int nextId = 1;
    public Truck(int container) {
        this.container = container;
        this.id = nextId;
        nextId++;
    }
    public void loadTruck() {
        synchronized (ConvoyerBelt.bricksOnBelt) {
            if (!ConvoyerBelt.bricksOnBelt.isEmpty()) {
                Brick brick = ConvoyerBelt.bricksOnBelt.remove();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ConvoyerBelt.belt.getItems().remove(0);
                    }
                });
                container += brick.mass;
                ConvoyerBelt.updateLabels(this);
                //System.out.println("Truck id: "+truck.id+" container: "+truck.container);
                if (container == ConvoyerBelt.TRUCK_CAPACITY) {
                    Truck newTruck = new Truck(0);
                    ConvoyerBelt.P1.setTruck(newTruck);
                    ConvoyerBelt.P2.setTruck(newTruck);
                    ConvoyerBelt.P3.setTruck(newTruck);
                    //System.out.println("Truck is full, new truck is ready.");
                }
            }
        }
    }
}
