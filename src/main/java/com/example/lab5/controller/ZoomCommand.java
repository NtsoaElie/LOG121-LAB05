package com.example.lab5.controller;

import com.example.lab5.model.Perspective;

public class ZoomCommand implements Command {

    private Perspective perspective;
    private double ancienZoom;
    private double facteur;

    public ZoomCommand(Perspective perspective, double facteur) {
        this.perspective = perspective;
        this.facteur = facteur;
    }

    @Override
    public void execute() {
        this.ancienZoom = perspective.getZoom();
        perspective.zoomer(facteur);
    }

    @Override
    public void undo() {
        perspective.setZoom(ancienZoom);
        perspective.notifyObservers();
    }
}
