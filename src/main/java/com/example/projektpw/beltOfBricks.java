package com.example.projektpw;

import java.util.LinkedList;

public class beltOfBricks {
    public LinkedList<Brick> bricksList = new LinkedList<Brick>();
    public synchronized void add(Brick brick){
        bricksList.add(brick);
    }
    public synchronized Brick remove(){
        return bricksList.removeFirst();
    }
    public Boolean isEmpty(){
        return bricksList.isEmpty();
    }
    public int getSize(){
        return bricksList.size();
    }
}
