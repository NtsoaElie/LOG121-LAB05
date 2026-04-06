package com.example.lab5.controller;

import com.example.lab5.model.Perspective;

public class MouseController {

    private Perspective perspective;

    // Le contrôleur a besoin de savoir quelle perspective il manipule
    public MouseController(Perspective perspective) {
        this.perspective = perspective;
    }

    // Méthode qui sera appelée par la Vue quand l'utilisateur bouge la souris
    public void gererTranslation(double dx, double dy) {
        // 1. Créer la commande
        Command translation = new TranslateCommand(perspective, dx, dy);
        // 2. L'envoyer à l'Invoker (le Singleton)
        Invoker.getInstance().executeCommand(translation);
    }

    // Méthode qui sera appelée par la Vue quand l'utilisateur utilise la molette
    public void gererZoom(double facteur) {
        Command zoom = new ZoomCommand(perspective, facteur);
        Invoker.getInstance().executeCommand(zoom);
    }
}