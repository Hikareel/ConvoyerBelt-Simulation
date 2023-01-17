package com.example.projektpw;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {

    public Truck truck;
    public  int BELT_CAPACITY;
    public  int BELT_WEIGHT_LIMIT;
    public  int TRUCK_CAPACITY;
    public static Label truckLoadLabel = new Label("Truck load: 0");
    public static Label truckCountLabel = new Label("Truck count: 0");
    public static Label bricksOnBeltLabel = new Label("Bricks on belt: 0");
    private Label P1label = new Label("P1");
    private Label P2label = new Label("P2");
    private Label P3label = new Label("P3");
    public static ListView<String> belt = new ListView<>();
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        readConfig();
        primaryStage.setTitle("Symulator taśmy transportowej");

        HBox root = new HBox();
        root.setPadding(new Insets(10));
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        VBox truckBoxMain = createTruckBoxMain();
        VBox bricksOnBeltBoxMain = createBricksOnBeltBoxMain();
        VBox workerBox = createWorkerBox();

        root.getChildren().addAll(truckBoxMain, bricksOnBeltBoxMain, workerBox);

        Scene scene = new Scene(root, 800, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        Truck truck = new Truck(0);
        Thread p1 = new Thread(new Worker(1, truck, BELT_CAPACITY, BELT_WEIGHT_LIMIT, TRUCK_CAPACITY));
        Thread p2 = new Thread(new Worker(2, truck, BELT_CAPACITY, BELT_WEIGHT_LIMIT, TRUCK_CAPACITY));
        Thread p3 = new Thread(new Worker(3, truck, BELT_CAPACITY, BELT_WEIGHT_LIMIT, TRUCK_CAPACITY));

        p1.start();
        p2.start();
        p3.start();
    }

    private VBox createTruckBoxMain(){
        VBox newBox = new VBox();
        HBox truckCountBox = new HBox();
        truckCountBox.setAlignment(Pos.TOP_RIGHT);
        truckCountBox.getChildren().addAll(truckCountLabel);
        HBox truckLoadBox = new HBox();
        truckLoadBox.setAlignment(Pos.BOTTOM_RIGHT);
        truckLoadBox.getChildren().addAll(truckLoadLabel);
        newBox.getChildren().addAll(truckCountBox,truckLoadBox);
        newBox.setSpacing(10);
        newBox.setAlignment(Pos.CENTER_LEFT);
        return newBox;
    }

    private VBox createBricksOnBeltBoxMain(){
        VBox newBox = new VBox();
        HBox beltBox = new HBox();
        beltBox.setStyle("-fx-border-color: brown; -fx-border-style: solid; -fx-border-width: 2px; -fx-border-radius: 5px;");
        beltBox.setAlignment(Pos.BASELINE_CENTER);
        beltBox.setMinSize(400, 100);
        beltBox.getChildren().add(belt);
        VBox beltLabel = new VBox();
        beltLabel.setSpacing(50);
        beltLabel.setAlignment(Pos.BOTTOM_CENTER);
        beltLabel.setPadding(new Insets(30));
        beltLabel.getChildren().addAll(bricksOnBeltLabel);
        newBox.getChildren().addAll(beltBox, beltLabel);
        return newBox;
    }

    private VBox createWorkerBox(){
        VBox newBox = new VBox();
        newBox.setSpacing(50);
        newBox.setAlignment(Pos.CENTER_RIGHT);
        newBox.getChildren().addAll(P1label, P2label, P3label);
        return newBox;
    }

    private void readConfig(){
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            BELT_CAPACITY = Integer.parseInt(prop.getProperty("BELT_CAPACITY"));
            BELT_WEIGHT_LIMIT = Integer.parseInt(prop.getProperty("BELT_WEIGHT_LIMIT"));
            TRUCK_CAPACITY = Integer.parseInt(prop.getProperty("TRUCK_CAPACITY"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}