package com.example.lab5.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Perspective implements Observable, Serializable {

    private static final long serialVersionUID = 1L;

    private double zoom = 1.0; // 1.0 = 100% (taille normale)
    private double transX = 0.0;
    private double transY = 0.0;

    private transient List<Observer> observers = new ArrayList<>();

    // Methode appelee par la TranslateCommand
    public void translater(double dx, double dy) {
        this.transX += dx;
        this.transY += dy;
        notifyObservers(); // Avertit la vue de se redessiner
    }

    // Methode appelee par la ZoomCommand
    public void zoomer(double facteur) {
        this.zoom *= facteur;
        notifyObservers(); // Avertit la vue de se redessiner
    }

    // --- Getters pour que la vue puisse lire les valeurs ---
    public double getZoom() { return zoom; }
    public double getTransX() { return transX; }
    public double getTransY() { return transY; }
    public void setZoom(double zoom) { this.zoom = zoom; }
    public void setTransX(double transX) { this.transX = transX; }
    public void setTransY(double transY) { this.transY = transY; }

    // --- Gestion du patron Observateur ---
    @Override
    public void addObserver(Observer o) {
        if (observers == null) observers = new ArrayList<>();
        observers.add(o);
    }

    public void reset() {
        this.zoom = 1.0;
        this.transX = 0.0;
        this.transY = 0.0;
        notifyObservers();
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
