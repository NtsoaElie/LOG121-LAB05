package com.example.lab5.controller;

import java.util.Stack;

public class Invoker {

    // 1. L'instance statique unique (Singleton)
    private static Invoker instance;

    // 2. L'historique pour le "Undo"
    private Stack<Command> historique = new Stack<>();

    // 3. Constructeur privé pour empêcher de faire un "new Invoker()"
    private Invoker() {}

    // 4. La méthode pour récupérer l'instance unique
    public static Invoker getInstance() {
        if (instance == null) {
            instance = new Invoker();
        }
        return instance;
    }

    public void executeCommand(Command c) {
        c.execute();
        historique.push(c); // On ajoute la commande à la pile après l'avoir exécutée
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