package com.example.lab5.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// On implemente Serializable pour pouvoir sauvegarder l'etat dans un fichier
public class ImageModel implements Observable, Serializable {

    private static final long serialVersionUID = 1L;

    private String imagePath;

    // Le mot-cle "transient" est CRUCIAL ici. Il dit a Java:
    // "Ne sauvegarde pas les interfaces graphiques (observateurs) dans le fichier de sauvegarde !"
    private transient List<Observer> observers = new ArrayList<>();

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        notifyObservers(); // Des qu'on change l'image, on crie aux vues de se mettre a jour !
    }

    @Override
    public void addObserver(Observer o) {
        if (observers == null) observers = new ArrayList<>(); // Securite apres un chargement
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
