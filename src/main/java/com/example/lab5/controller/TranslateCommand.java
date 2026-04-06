package com.example.lab5.controller;

import com.example.lab5.model.Perspective;

public class TranslateCommand implements Command {

    private Perspective perspective;
    private double dx;
    private double dy;

    public TranslateCommand(Perspective perspective, double dx, double dy) {
        this.perspective = perspective;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        perspective.translater(dx, dy);
    }

    @Override
    public void undo() {
        // L'inverse d'une addition est une soustraction !
        perspective.translater(-dx, -dy);
    }
}