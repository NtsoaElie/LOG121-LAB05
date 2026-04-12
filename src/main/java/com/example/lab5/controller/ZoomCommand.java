package com.example.lab5.controller;

import com.example.lab5.model.Perspective;

public class ZoomCommand implements Command {

    private Perspective perspective;
    private double ancienZoom; // On sauvegarde l'état avant de modifier !
    private double facteur;

    public ZoomCommand(Perspective perspective, double facteur) {
        this.perspective = perspective;
        this.facteur = facteur;
    }

    @Override
    public void execute() {
        // Avant de zoomer, on prend une "photo" de l'ancien zoom
        this.ancienZoom = perspective.getZoom();
        perspective.zoomer(facteur);
    }

    @Override
    public void undo() {
        perspective.setZoom(ancienZoom);
        perspective.notifyObservers();
        // Pour annuler, on écrase le zoom actuel avec l'ancien zoom sauvegardé
        // (Il faut ajouter un setter dans Perspective pour ça, ou diviser par le facteur)
        //perspective.zoomer(1 / facteur); // L'inverse mathématique d'une multiplication
    }
}
