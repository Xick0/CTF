package dev.marco.xicko.Bot;
import dev.marco.xicko.Collections.Grafos.NetworkBiDirectional;
import dev.marco.xicko.Collections.Grafos.NetworkUniDirectional;

import java.util.Iterator;


public class Bot {
    private String name;
    private int currentPosition;
   private Iterator<Integer> strategy;


    public Bot(String name,Iterator<Integer> strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Iterator<Integer> getIterator() {
        return strategy;
    }

    public void setIterator(Iterator<Integer> iterator) {
        this.strategy = iterator;
    }


}

