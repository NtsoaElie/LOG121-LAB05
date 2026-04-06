package com.example.lab5.model;

public interface Observable {
    void addObserver(Observer o);
    void notifyObservers();
}