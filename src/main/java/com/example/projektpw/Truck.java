package com.example.projektpw;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        synchronized (Main.bricksOnBelt) {
            if (!Main.bricksOnBelt.isEmpty()) {
                Brick brick = Main.bricksOnBelt.remove();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.belt.getItems().remove(0);
                    }
                });
                container += brick.mass;
                Main.updateLabels(this);
                //System.out.println("Truck id: "+truck.id+" container: "+truck.container);
                if (container == Main.TRUCK_CAPACITY) {
                    Truck newTruck = new Truck(0);
                    Main.P1.setTruck(newTruck);
                    Main.P2.setTruck(newTruck);
                    Main.P3.setTruck(newTruck);
                    //System.out.println("Truck is full, new truck is ready.");
                }
            }
        }
    }
}
