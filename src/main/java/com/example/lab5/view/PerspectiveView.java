package com.example.lab5.view;

import com.example.lab5.controller.MouseController;
import com.example.lab5.model.ImageModel;
import com.example.lab5.model.Observer;
import com.example.lab5.model.Perspective;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class PerspectiveView extends Pane implements Observer {

    private ImageModel imageModel;
    private Perspective perspective;
    private MouseController mouseController;
    private ImageView imageView;

    // Variables pour calculer la distance du glissement de la souris
    private double lastMouseX;
    private double lastMouseY;

    public PerspectiveView(ImageModel imageModel, Perspective perspective, MouseController mouseController) {
        this.imageModel = imageModel;
        this.perspective = perspective;
        this.mouseController = mouseController;

        this.imageView = new ImageView();
        this.getChildren().add(imageView);

        // Optionnel mais recommandé : empêche l'image de déborder visuellement de sa zone
        this.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        initialiserEvenementsSouris();
    }

    private void initialiserEvenementsSouris() {
        // 1. Quand on clique, on enregistre la position de départ
        this.setOnMousePressed(e -> {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        });

        // 2. Quand on glisse (Translation)
        this.setOnMouseDragged(e -> {
            double dx = e.getX() - lastMouseX;
            double dy = e.getY() - lastMouseY;
            // On DÉLÈGUE au contrôleur, la vue ne modifie jamais le modèle !
            mouseController.gererTranslation(dx, dy);

            // On met à jour la dernière position
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        });

        // 3. Quand on roule la molette (Zoom)
        this.setOnScroll(e -> {
            // Si deltaY est positif (haut), on zoom in (1.1). Sinon, zoom out (0.9).
            double facteur = e.getDeltaY() > 0 ? 1.1 : 0.9;
            mouseController.gererZoom(facteur);
        });
    }

    @Override
    public void update() {
        // 1. Mettre à jour l'image si elle a changé
        if (imageModel.getImagePath() != null) {
            Image img = new Image(new File(imageModel.getImagePath()).toURI().toString());
            imageView.setImage(img);
        }

        // 2. Mettre à jour la perspective (Position et Zoom)
        imageView.setTranslateX(perspective.getTransX());
        imageView.setTranslateY(perspective.getTransY());
        imageView.setScaleX(perspective.getZoom());
        imageView.setScaleY(perspective.getZoom());
    }
}