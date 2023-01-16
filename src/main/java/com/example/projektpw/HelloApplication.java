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
    public Truck truck;
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

        Truck truck = new Truck(0);
        Thread p1 = new Thread(new Worker(1, truck));
        Thread p2 = new Thread(new Worker(2, truck));
        Thread p3 = new Thread(new Worker(3, truck));


        p1.start();
        p2.start();
        p3.start();
    }
    private void updateLabels() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                truckLoadLabel.setText("Truck load: ");
                bricksOnBeltLabel.setText("Bricks on belt: ");
            }
        });
    }
}