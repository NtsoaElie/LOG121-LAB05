package com.example.lab5.controller;

import com.example.lab5.model.ImageModel;
import com.example.lab5.model.Perspective;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;

public class SaveCommand implements Command {

    private ImageModel imageModel;
    private Perspective perspective1;
    private Perspective perspective2;
    private File fichierDestination;

    public SaveCommand(ImageModel img, Perspective p1, Perspective p2, File fichier) {
        this.imageModel = img;
        this.perspective1 = p1;
        this.perspective2 = p2;
        this.fichierDestination = fichier;
    }

    @Override
    public void execute() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierDestination))) {
            oos.writeObject(imageModel);
            oos.writeObject(perspective1);
            oos.writeObject(perspective2);
            System.out.println("Sauvegarde réussie dans : " + fichierDestination.getName());
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    @Override
    public void undo() {
        System.out.println("Impossible d'annuler une sauvegarde sur le disque.");
    }
}
