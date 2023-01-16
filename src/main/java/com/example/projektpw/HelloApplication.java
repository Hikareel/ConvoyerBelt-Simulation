package com.example.projektpw;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class HelloApplication extends Application {
    private static final int TRUCK_CAPACITY = 50;
    private static final int BELT_CAPACITY = 10;
    private static final int BELT_WEIGHT_LIMIT = 30;

    private int truckLoad = 0;
    private LinkedList<Integer> bricksOnBelt = new LinkedList<Integer>();
    private Label truckLoadLabel = new Label("Truck load: 0");
    private Label bricksOnBeltLabel = new Label("Bricks on belt: 0");

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Conveyor Belt Simulator");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        HBox truckLoadBox = new HBox();
        truckLoadBox.setSpacing(10);
        truckLoadBox.setAlignment(Pos.CENTER);
        truckLoadBox.getChildren().addAll(truckLoadLabel);

        HBox bricksOnBeltBox = new HBox();
        bricksOnBeltBox.setSpacing(10);
        bricksOnBeltBox.setAlignment(Pos.CENTER);
        bricksOnBeltBox.getChildren().addAll(bricksOnBeltLabel);

        root.getChildren().addAll(truckLoadBox, bricksOnBeltBox);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread p1 = new Thread(new Worker(1));
        Thread p2 = new Thread(new Worker(2));
        Thread p3 = new Thread(new Worker(3));
        Thread truck = new Thread(new Truck());
        p1.start();
        p2.start();
        p3.start();
        truck.start();
    }

    private class Worker implements Runnable {
        private int mass;

        public Worker(int mass) {
            this.mass = mass;
        }

        @Override
        public void run() {
            while (true) {
                if (bricksOnBelt.size() <= BELT_CAPACITY) {
                    layBrick(mass);
                }
            }
        }
    }

    private void layBrick(int mass) {
        synchronized (bricksOnBelt) {
            int weight = 0;
            for(int brick : bricksOnBelt) weight += brick;
            if (bricksOnBelt.size() < BELT_CAPACITY && weight + mass <= BELT_WEIGHT_LIMIT) {
                bricksOnBelt.add(mass);
                updateLabels();
            }
        }
    }

    private void updateLabels() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                truckLoadLabel.setText("Truck load: " + truckLoad);
                bricksOnBeltLabel.setText("Bricks on belt: " + bricksOnBelt.size());
            }
        });
    }

    private class Truck implements Runnable {

        private int truckLoad = 0;

        @Override
        public void run() {
            while(true){
                loadTruck();
                updateLabels();
            }
        }
    }

    private void loadTruck() {
        synchronized (bricksOnBelt) {
            if(!bricksOnBelt.isEmpty()) {
                int brick = bricksOnBelt.removeFirst();
                truckLoad += brick;
                if (truckLoad >= TRUCK_CAPACITY - 2 && truckLoad <= TRUCK_CAPACITY) {
                    truckLoad = 0;
                    System.out.println("Truck is full, new truck is ready.");
                    updateLabels();
                }
            }
        }
    }


}