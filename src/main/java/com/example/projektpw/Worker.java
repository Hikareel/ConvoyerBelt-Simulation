package com.example.projektpw;

import javafx.application.Platform;

import static com.example.projektpw.ConvoyerBelt.bricksOnBelt;

public class Worker implements Runnable{
    public String name;
    public int brickType;
    public int BELT_CAPACITY;
    public int BELT_WEIGHT_LIMIT;
    public int TRUCK_CAPACITY;
    private int takeBrickTime;
    public void setTruck(Truck truck) {
        this.truck = truck;
    }
    private Truck truck;
    public Worker(int brickType, Truck truck, int BELT_CAPACITY, int BELT_WEIGHT_LIMIT, int TRUCK_CAPACITY) {
        this.brickType = brickType;
        this.truck = truck;
        this.BELT_CAPACITY = BELT_CAPACITY;
        this.BELT_WEIGHT_LIMIT = BELT_WEIGHT_LIMIT;
        this.TRUCK_CAPACITY = TRUCK_CAPACITY;
        this.takeBrickTime = brickType*100;
    }
    @Override
    public void run() {
        while (true) {
            //System.out.println("-------"+HelloApplication.bricksOnBelt.size());
            try {
                Thread.sleep(takeBrickTime);
                ConvoyerBelt.lock.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            layBrick(brickType);
            ConvoyerBelt.lock.release();
        }
    }
    private synchronized void layBrick(int mass) {
        synchronized (bricksOnBelt) {
            int weight = 0;
            for(Brick brick : bricksOnBelt.bricksList) {
                weight += brick.mass;
            }
            //System.out.println("Belt weight: "+weight+" Worker P"+mass+" working..");
            if (bricksOnBelt.getSize() < BELT_CAPACITY &&
                    weight + mass <= BELT_WEIGHT_LIMIT &&
                    truck.container + weight + mass <= TRUCK_CAPACITY) {
                Brick brick = new Brick(brickType);
                bricksOnBelt.add(brick);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ConvoyerBelt.belt.getItems().add(String.valueOf(mass));
                    }
                });
                ConvoyerBelt.updateLabels(truck);
                //System.out.println("brick layed by P"+ brickType+", belt weight: "+(weight+mass)+"");
            }else {
                truck.loadTruck();
            }
            try{
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
