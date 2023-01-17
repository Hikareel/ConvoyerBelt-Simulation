package com.example.projektpw;

import javafx.application.Platform;

public class Worker implements Runnable{
    public String name;
    public int brickType;
    public int BELT_CAPACITY;
    public int BELT_WEIGHT_LIMIT;
    public int TRUCK_CAPACITY;
    private Truck truck;


    public Worker(int brickType, Truck truck, int BELT_CAPACITY, int BELT_WEIGHT_LIMIT, int TRUCK_CAPACITY) {
        this.brickType = brickType;
        this.truck = truck;
        this.BELT_CAPACITY = BELT_CAPACITY;
        this.BELT_WEIGHT_LIMIT = BELT_WEIGHT_LIMIT;
        this.TRUCK_CAPACITY = TRUCK_CAPACITY;
    }

    @Override
    public void run() {
        while (true) {
            //System.out.println("-------"+HelloApplication.bricksOnBelt.size());
            layBrick(brickType);
        }
    }

    private void layBrick(int mass) {
        synchronized (truck.bricksOnBelt) {

            int weight = 0;
            for(Brick brick : truck.bricksOnBelt) {
                weight += brick.mass;
            }
            //System.out.println("Belt weight: "+weight+" Worker P"+mass+" working..");
            if (truck.bricksOnBelt.size() < BELT_CAPACITY &&
                    weight + mass <= BELT_WEIGHT_LIMIT &&
                    truck.container + weight + mass <= TRUCK_CAPACITY) {
                Brick brick = new Brick(brickType);
                truck.bricksOnBelt.add(brick);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HelloApplication.belt.getItems().add(String.valueOf(mass));
                    }
                });
                updateLabels();
                //System.out.println("brick layed by P"+ brickType+", belt weight: "+(weight+mass)+"");
            }else {
                loadTruck();
            }
            try{
            Thread.sleep(500);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void loadTruck() {
        synchronized (truck.bricksOnBelt) {
            if (!truck.bricksOnBelt.isEmpty()) {
                Brick brick = truck.bricksOnBelt.removeFirst();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HelloApplication.belt.getItems().remove(0);
                    }
                });
                truck.container += brick.mass;
                updateLabels();
                //System.out.println("Truck id: "+truck.id+" container: "+truck.container);
                if (truck.container == TRUCK_CAPACITY) {
                    truck = new Truck(0);
                    //System.out.println("Truck is full, new truck is ready.");
                }
            }
        }
    }
    public void updateLabels() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HelloApplication.truckLoadLabel.setText("Truck load: " + truck.container);
                HelloApplication.bricksOnBeltLabel.setText("Bricks on belt: " + truck.bricksOnBelt.size());
                HelloApplication.truckCountLabel.setText("Truck count: " + (truck.id));
            }
        });
    }
}
