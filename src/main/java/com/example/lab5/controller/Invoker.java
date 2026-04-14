package com.example.lab5.controller;

import java.util.Stack;

public class Invoker {
    private static Invoker instance;
    private Stack<Command> historique = new Stack<>();

    private Invoker() {}

    public static Invoker getInstance() {
        if (instance == null) {
            instance = new Invoker();
        }
        return instance;
    }

    public void executeCommand(Command c) {
        c.execute();
        historique.push(c);
    }

    public void undo() {
        if (!historique.isEmpty()) {
            Command derniereCommande = historique.pop();
            derniereCommande.undo();
        } else {
            System.out.println("Rien à annuler.");
        }
    }
}
