package com.example.lab5.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageModel implements Observable, Serializable {

    private static final long serialVersionUID = 1L;

    private String imagePath;

    private transient List<Observer> observers = new ArrayList<>();

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        if (observers == null) observers = new ArrayList<>();
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        if (observers != null) {
            for (Observer o : observers) {
                o.update();
            }
        }
    }
}
