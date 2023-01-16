package com.example.projektpw;

public class Worker implements Runnable{
    public String name;
    public int brickType;
    private static final int BELT_CAPACITY = 10;
    private static final int BELT_WEIGHT_LIMIT = 30;
    private static final int TRUCK_CAPACITY = 50;
    private Truck truck;


    public Worker(int brickType, Truck truck) {
        this.brickType = brickType;
        this.truck = truck;
    }

    @Override
    public void run() {

        while (true) {
            //System.out.println("-------"+HelloApplication.bricksOnBelt.size());
            layBrick(brickType);
            //System.out.println("Prac"+brickType);

        }
    }

    private void layBrick(int mass) {
        synchronized (truck.bricksOnBelt) {
            int weight = 0;
            for(Brick brick : truck.bricksOnBelt) weight += brick.mass;

            if (truck.bricksOnBelt.size() < BELT_CAPACITY && weight + mass <= BELT_WEIGHT_LIMIT && truck.container + mass <= TRUCK_CAPACITY) {
                Brick brick = new Brick(brickType);
                truck.bricksOnBelt.add(brick);
                //System.out.println("brick layed: "+ brickType);
            }
            loadTruck();
        }
    }
    public void loadTruck() {
        //synchronized (truck.bricksOnBelt) {
            if(!truck.bricksOnBelt.isEmpty()) {
                Brick brick = truck.bricksOnBelt.removeFirst();
                truck.container += brick.mass;
                System.out.println(truck.container);
                if (truck.container == TRUCK_CAPACITY) {
                    truck = new Truck(0);
                    System.out.println("Truck is full, new truck is ready.");
                }
            }
        //}
    }
}
