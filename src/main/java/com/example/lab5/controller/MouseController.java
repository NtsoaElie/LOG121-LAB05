package com.example.lab5.controller;

import com.example.lab5.model.Perspective;

public class MouseController {

    private Perspective perspective;


    public MouseController(Perspective perspective) {
        this.perspective = perspective;
    }

    public void gererTranslation(double dx, double dy) {
        Command translation = new TranslateCommand(perspective, dx, dy);
        Invoker.getInstance().executeCommand(translation);
    }

    public void gererZoom(double facteur) {
        Command zoom = new ZoomCommand(perspective, facteur);
        Invoker.getInstance().executeCommand(zoom);
    }
}
